package istanbul.codify.muudy.ui.welcome;

import istanbul.codify.muudy.ui.base.MvpView;

interface WelcomeView extends MvpView {

    void onRegisterWithEmailClicked();

    void onRegisterWithFacebookClicked();

    void onLoginClicked();
}
