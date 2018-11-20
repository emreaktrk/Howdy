package istanbul.codify.monju.ui.login;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Credential;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

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
