package com.codify.howdy.ui.welcome;

import com.codify.howdy.ui.base.MvpView;

interface WelcomeView extends MvpView {

    void onRegisterWithEmailClicked();

    void onRegisterWithFacebookClicked();

    void onLoginClicked();
}
