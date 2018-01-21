package com.codify.howdy.ui.login;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Credential;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class LoginPresenter extends BasePresenter<LoginView> {

    @Override
    public void attachView(LoginView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.login_login))
                        .subscribe(o -> {
                            AppCompatEditText username = root.findViewById(R.id.login_username);
                            AppCompatEditText password = root.findViewById(R.id.login_password);
                            Credential credential = new Credential(username.getText().toString(), password.getText().toString());

                            Logcat.v("Login clicked with credentials " + credential.toString());
                            view.onLoginClicked(credential);
                        }));

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.login_connect_with_facebook))
                        .subscribe(o -> {
                            Logcat.v("Connect with facebook clicked");
                            view.onConnectWithFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.login_forgot_password))
                        .subscribe(o -> {
                            Logcat.v("Forgot password clicked");
                            view.onConnectWithFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.login_register))
                        .subscribe(o -> {
                            Logcat.v("Register clicked");
                            view.onRegisterClicked();
                        }));
    }
}
