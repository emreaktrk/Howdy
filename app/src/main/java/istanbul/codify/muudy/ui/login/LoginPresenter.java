package istanbul.codify.muudy.ui.login;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.LoginRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.LoginResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Credential;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class LoginPresenter extends BasePresenter<LoginView> {

    @Override
    public void attachView(LoginView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.login_login))
                        .mergeWith(RxTextView.editorActions(findViewById(R.id.login_password)))
                        .subscribe(o -> {
                            AppCompatEditText username = findViewById(R.id.login_email);
                            AppCompatEditText password = findViewById(R.id.login_password);
                            Credential credential = new Credential(username
                                    .getText()
                                    .toString(), password
                                    .getText()
                                    .toString());

                            Logcat.v("Login clicked with credentials " + credential.toString());
                            view.onLoginClicked(credential);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.login_connect_with_facebook))
                        .subscribe(o -> {
                            Logcat.v("Connect with facebook clicked");
                            view.onConnectWithFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.login_forgot_password))
                        .subscribe(o -> {
                            Logcat.v("Forgot password clicked");
                            view.onConnectWithFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.login_register))
                        .subscribe(o -> {
                            Logcat.v("Register clicked");
                            view.onRegisterClicked();
                        }));
    }

    public void login(Credential credential) {
        LoginRequest request = new LoginRequest(credential);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .login(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<LoginResponse>() {
                            @Override
                            protected void success(LoginResponse response) {
                                mView.onLogin(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
