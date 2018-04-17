package istanbul.codify.muudy.ui.register;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.RegisterForm;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface RegisterView extends MvpView {

    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);

    void onRegister(User user);

    void onError(ApiError error);
}
