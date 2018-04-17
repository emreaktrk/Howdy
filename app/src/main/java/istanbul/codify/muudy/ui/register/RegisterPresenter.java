package istanbul.codify.muudy.ui.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;

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
                        .clicks(findViewById(R.id.register_register))
                        .filter(o -> {
                            TextInputEditText password = findViewById(R.id.register_password);
                            TextInputEditText confirm = findViewById(R.id.register_password_confirm);
                            return StringUtils.equals(password.getText(), confirm.getText());
                        })
                        .filter(o -> {
                            CharSequence email = findViewById(R.id.register_email, TextInputEditText.class).getText();
                            return new Email(email).isValid();
                        })
                        .subscribe(o -> {
                            RegisterForm form = new RegisterForm();
                            form.mFullname = findViewById(R.id.register_fullname, TextInputEditText.class).getText();
                            form.mGender = Gender.MALE;
                            form.mBirthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText();
                            form.mEmail = new Email(findViewById(R.id.register_email, TextInputEditText.class).getText());
                            form.mUsername = findViewById(R.id.register_username, TextInputEditText.class).getText();
                            form.mPassword = findViewById(R.id.register_password, TextInputEditText.class).getText();
                            form.mPasswordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            view.onRegisterClicked(form);
                        }));
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
