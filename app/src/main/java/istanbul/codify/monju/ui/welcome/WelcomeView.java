package istanbul.codify.monju.ui.welcome;

import istanbul.codify.monju.ui.base.MvpView;

interface WelcomeView extends MvpView {

    void onRegisterWithEmailClicked();

    void onRegisterWithFacebookClicked();

    void onLoginClicked();
}
