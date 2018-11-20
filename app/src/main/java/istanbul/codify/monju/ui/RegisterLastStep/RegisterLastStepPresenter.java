package istanbul.codify.monju.ui.RegisterLastStep;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.RegisterRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.RegisterResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Gender;
import istanbul.codify.monju.model.RegisterForm;
import istanbul.codify.monju.ui.base.BasePresenter;

/**
 * Created by egesert on 31.08.2018.
 */

public class RegisterLastStepPresenter extends BasePresenter<RegisterLastStepView> {

    private int mGender = -1;

    RegisterForm mForm;

    @Override
    public void attachView(RegisterLastStepView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_last_step_close))
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_last_step_birthday, TextInputEditText.class))
                        .subscribe(o -> {
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                                TextInputEditText birthday = findViewById(R.id.register_last_step_birthday, TextInputEditText.class);
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
                        .clicks(findViewById(R.id.register_last_step_gender, TextInputEditText.class))
                        .subscribe(o -> showGenderSelectDialog()));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_last_step_register))

                        .filter(o -> checkFields())
                        .subscribe(o -> {
                            mForm.mFullname = findViewById(R.id.register_last_step_fullname, TextInputEditText.class).getText();
                            mForm.mGender = mGender == 0 ? Gender.MALE : Gender.FEMALE;
                            mForm.mBirthday = findViewById(R.id.register_last_step_birthday, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            view.onRegisterClicked(mForm);
                        }));
    }

    void bind(RegisterForm form){
        mForm = form;
    }
    private boolean checkFields() {
        String fullName = findViewById(R.id.register_last_step_fullname, TextInputEditText.class).getText().toString().trim();
        String gender = findViewById(R.id.register_last_step_gender, TextInputEditText.class).getText().toString().trim();
        String birthday = findViewById(R.id.register_last_step_birthday, TextInputEditText.class).getText().toString().trim();


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
                    findViewById(R.id.register_last_step_gender, TextInputEditText.class).setText(gender);
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
