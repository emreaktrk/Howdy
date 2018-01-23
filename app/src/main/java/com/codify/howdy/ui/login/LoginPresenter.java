package com.codify.howdy.ui.login;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.LoginRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.LoginResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Credential;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class LoginPresenter extends BasePresenter<LoginView> {

    @Override
    public void attachView(LoginView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.login_login))
                        .subscribe(o -> {
                            AppCompatEditText username = findViewById(R.id.login_username);
                            AppCompatEditText password = findViewById(R.id.login_password);
                            Credential credential = new Credential(username.getText().toString(), password.getText().toString());

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
                        .doOnError(Logcat::e)
                        .subscribe(new ServiceConsumer<LoginResponse>() {
                            @Override
                            protected void success(LoginResponse response) {
                                // TODO Pass user model taken by response to onLogin
                                mView.onLogin(null);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error.message);

                                mView.onError(error);
                            }
                        }));
    }
}
