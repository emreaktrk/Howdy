package istanbul.codify.muudy.ui.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import android.widget.ArrayAdapter;
import com.blankj.utilcode.util.StringUtils;
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
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class RegisterPresenter extends BasePresenter<RegisterView> {

    private int selectedGenderIndex = -1;
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
                                TextInputEditText birthday = findViewById(R.id.register_birthday);
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
                        .subscribe(o -> {
                            showGenderSelectDialog(root.getContext());
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_register))
                    /*    .filter(o -> {
                            TextInputEditText password = findViewById(R.id.register_password);
                            TextInputEditText confirm = findViewById(R.id.register_password_confirm);
                            return StringUtils.equals(password.getText(), confirm.getText());
                        })
                        .filter(o -> {
                            CharSequence email = findViewById(R.id.register_email, TextInputEditText.class).getText();
                            return new Email(email).isValid();
                        }*/
                        .filter(o -> {
                            return checkFields();
                        })
                        .subscribe(o -> {
                            RegisterForm form = new RegisterForm();
                            form.mFullname = findViewById(R.id.register_fullname, TextInputEditText.class).getText();
                            form.mGender = selectedGenderIndex == 0 ? Gender.MALE : Gender.FEMALE;
                            form.mBirthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText();
                            form.mEmail = new Email(findViewById(R.id.register_email, TextInputEditText.class).getText());
                            form.mUsername = findViewById(R.id.register_username, TextInputEditText.class).getText();
                            form.phone = findViewById(R.id.register_phone, TextInputEditText.class).getText();
                            form.mPassword = findViewById(R.id.register_password, TextInputEditText.class).getText();
                            form.mPasswordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            view.onRegisterClicked(form);
                        }));
    }

    private boolean checkFields(){
        String fullName = findViewById(R.id.register_fullname, TextInputEditText.class).getText().toString().trim();
        String gender = findViewById(R.id.register_gender, TextInputEditText.class).getText().toString().trim();
        String birthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText().toString().trim();
        String email = findViewById(R.id.register_email, TextInputEditText.class).getText().toString().trim();
        String username = findViewById(R.id.register_username, TextInputEditText.class).getText().toString().trim();
        String phone = findViewById(R.id.register_phone, TextInputEditText.class).getText().toString().trim();
        String password = findViewById(R.id.register_password, TextInputEditText.class).getText().toString().trim();
        String passwordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText().toString().trim();

        if (fullName.length() == 0) {
            showAlert("İsim soyisim boş olamaz");
            return false;
        }

        if (gender.length() == 0) {
            showAlert("Cinsiyet boş olamaz");
            return false;
        }

        if (birthday.length() == 0) {
            showAlert("Doğum Tarihi boş olamaz");
            return false;
        }

        if (email.length() == 0) {
            showAlert("E-posta boş olamaz");
            return false;
        }

        if (!new Email(email).isValid()){
            showAlert("Girilen E-posta adresi geçersiz.");
            return false;
        }

        if (username.length() == 0) {
            showAlert("Kullanıcı Adı boş olamaz");
            return false;
        }
/*
        if (phone.length() == 0) {
            showAlert("İsim soyisim boş olamaz");
            return false;
        }*/

        if (password.length() == 0) {
            showAlert("Şifre boş olamaz");
            return false;
        }

        if (passwordConfirm.length() == 0) {
            showAlert("Şifre tekrarı boş olamaz");
            return false;
        }

        if (!StringUtils.equals(password, passwordConfirm)) {
            showAlert("Girilen şifreler birbiriyle uyuşmuyor");
            return false;
        }

        return true;
    }

    private void showAlert(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mRoot.getContext());
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Tamam",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void showGenderSelectDialog(Context context){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Cinsiyet Seçiniz");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Erkek");
        arrayAdapter.add("Kadın");

        builderSingle.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gender = arrayAdapter.getItem(which);
                selectedGenderIndex = which;
                findViewById(R.id.register_gender, TextInputEditText.class).setText(gender);
            }
        });
        builderSingle.show();
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
