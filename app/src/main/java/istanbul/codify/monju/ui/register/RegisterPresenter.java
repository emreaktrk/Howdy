package istanbul.codify.monju.ui.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.CheckEmailRequest;
import istanbul.codify.monju.api.pojo.request.CheckUsernameRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.CheckUsernameResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Email;
import istanbul.codify.monju.model.RegisterForm;
import istanbul.codify.monju.ui.base.BasePresenter;

final class RegisterPresenter extends BasePresenter<RegisterView> {

    private int mGender = -1;

    String mUsername;

    RegisterForm form;

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
                        .clicks(findViewById(R.id.register_register))

                        .filter(o -> checkFields())
                        .subscribe(o -> {
                            RegisterForm form = new RegisterForm();

                            form.mEmail = new Email(findViewById(R.id.register_email, TextInputEditText.class).getText());

                            form.mPassword = findViewById(R.id.register_password, TextInputEditText.class).getText();
                            form.mPasswordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            checkEmail(form);
                        }));

    }

    private boolean checkFields() {

        String email = findViewById(R.id.register_email, TextInputEditText.class).getText().toString().trim();



        String password = findViewById(R.id.register_password, TextInputEditText.class).getText().toString().trim();
        String passwordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText().toString().trim();


        if (StringUtils.isEmpty(email)) {
            showAlert("Email boş olamaz");
            return false;
        }

        if (!RegexUtils.isEmail(email)) {
            showAlert("Girilen Email adresi geçersiz.");
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

    public void register(RegisterForm form) {

        int index = form.mEmail.mValue.toString().indexOf("@");
        String username = form.mEmail.mValue.toString().substring(0, index);
        form.mUsername = username;
        checkUsername(form,username);

    }

    public void checkUsername(RegisterForm form, String username){


        CheckUsernameRequest request = new CheckUsernameRequest();
        request.username = username;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .checkUsername(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CheckUsernameResponse>() {
                            @Override
                            protected void success(CheckUsernameResponse response) {
                                if (response.data.isAlreadyUsed) {
                                    form.mUsername = response.data.recommendedUsername;

                                }
                                mView.onRegisterClicked(form);


                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                form.mUsername = username+"1413";
                                mView.onRegisterClicked(form);
                                mView.onError(error);
                            }
                        }));
    }

    public void checkEmail(RegisterForm form){
        CheckEmailRequest request = new CheckEmailRequest();
        request.email = form.mEmail.mValue.toString();

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .checkEmail(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CheckUsernameResponse>() {
                            @Override
                            protected void success(CheckUsernameResponse response) {
                                if (response.data.isAlreadyUsed) {
                                    showAlert("Girdiğiniz email adresi kullanımda");

                                }else{
                                    register(form);
                                }



                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mView.onError(error);
                            }
                        }));
    }
}
