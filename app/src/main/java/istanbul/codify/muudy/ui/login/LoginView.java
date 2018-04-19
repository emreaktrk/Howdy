package istanbul.codify.muudy.ui.login;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Credential;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface LoginView extends MvpView {

    void onLoginClicked(Credential credential);

    void onConnectWithFacebookClicked();

    void onCreateUser(User user);

    void onForgotPasswordClicked();

    void onRegisterClicked();

    void onCloseClicked();

    void onLogin(User user);

    void onError(ApiError error);

}
