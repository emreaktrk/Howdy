package com.codify.howdy.ui.login;

import com.codify.howdy.model.Credential;
import com.codify.howdy.ui.base.MvpView;

interface LoginView extends MvpView {

    void onLoginClicked(Credential credential);

    void onConnectWithFacebookClicked();

    void onForgotPasswordClicked();

    void onRegisterClicked();

    void onCloseClicked();
}
