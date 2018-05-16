package istanbul.codify.muudy.ui.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.RegisterRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.RegisterResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Email;
import istanbul.codify.muudy.model.Gender;
import istanbul.codify.muudy.model.RegisterForm;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.Calendar;

final class RegisterPresenter extends BasePresenter<RegisterView> {

    private int mGender = -1;

    @SuppressLint("CutPasteId")
    @Override
    public void attachView(RegisterView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_close))
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_birthday, TextInputEditText.class))
                        .subscribe(o -> {
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                                TextInputEditText birthday = findViewById(R.id.register_birthday, TextInputEditText.class);
                                birthday.setText(
                                        new StringBuilder()
                                                .append(year)
                                                .append("-")
                                                .append(month + 1)
                                                .append("-")
                                                .append(day));
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            dialog.show();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_gender, TextInputEditText.class))
                        .subscribe(o -> showGenderSelectDialog()));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_register))

                        .filter(o -> checkFields())
                        .subscribe(o -> {
                            RegisterForm form = new RegisterForm();
                            form.mFullname = findViewById(R.id.register_fullname, TextInputEditText.class).getText();
                            form.mGender = mGender == 0 ? Gender.MALE : Gender.FEMALE;
                            form.mBirthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText();
                            form.mEmail = new Email(findViewById(R.id.register_email, TextInputEditText.class).getText());
                            form.mUsername = findViewById(R.id.register_username, TextInputEditText.class).getText();
                            form.mPhone = findViewById(R.id.register_phone, TextInputEditText.class).getText();
                            form.mPassword = findViewById(R.id.register_password, TextInputEditText.class).getText();
                            form.mPasswordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            view.onRegisterClicked(form);
                        }));
    }

    private boolean checkFields() {
        String fullName = findViewById(R.id.register_fullname, TextInputEditText.class).getText().toString().trim();
        String gender = findViewById(R.id.register_gender, TextInputEditText.class).getText().toString().trim();
        String birthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText().toString().trim();
        String email = findViewById(R.id.register_email, TextInputEditText.class).getText().toString().trim();
        String username = findViewById(R.id.register_username, TextInputEditText.class).getText().toString().trim();
        String phone = findViewById(R.id.register_phone, TextInputEditText.class).getText().toString().trim();
        String password = findViewById(R.id.register_password, TextInputEditText.class).getText().toString().trim();
        String passwordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText().toString().trim();

        if (StringUtils.isEmpty(fullName)) {
            showAlert("İsim soyisim boş olamaz");
            return false;
        }

        if (StringUtils.isEmpty(gender)) {
            showAlert("Cinsiyet boş olamaz");
            return false;
        }

        if (StringUtils.isEmpty(birthday)) {
            showAlert("Doğum Tarihi boş olamaz");
            return false;
        }

        if (StringUtils.isEmpty(email)) {
            showAlert("Email boş olamaz");
            return false;
        }

        if (!RegexUtils.isEmail(email)) {
            showAlert("Girilen Email adresi geçersiz.");
            return false;
        }

        if (StringUtils.isEmpty(username)) {
            showAlert("Kullanıcı Adı boş olamaz");
            return false;
        }

        if (StringUtils.isEmpty(password)) {
            showAlert("Şifre boş olamaz");
            return false;
        }

        if (StringUtils.isEmpty(passwordConfirm)) {
            showAlert("Şifre tekrarı boş olamaz");
            return false;
        }

        if (!StringUtils.equals(password, passwordConfirm)) {
            showAlert("Girilen şifreler birbiriyle uyuşmuyor");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new AlertDialog
                .Builder(getContext())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Tamam", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showGenderSelectDialog() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
        adapter.add("Erkek");
        adapter.add("Kadın");

        new AlertDialog
                .Builder(getContext())
                .setTitle("Cinsiyet Seçiniz")
                .setNegativeButton("İptal", (dialog, which) -> dialog.dismiss())
                .setAdapter(adapter, (dialog, which) -> {
                    String gender = adapter.getItem(which);
                    mGender = which;
                    findViewById(R.id.register_gender, TextInputEditText.class).setText(gender);
                })
                .setNegativeButton("İptal", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public void register(RegisterForm form) {
        RegisterRequest request = new RegisterRequest(form);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .register(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<RegisterResponse>() {
                            @Override
                            protected void success(RegisterResponse response) {
                                mView.onRegister(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
