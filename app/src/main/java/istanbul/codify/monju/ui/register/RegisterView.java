package istanbul.codify.monju.ui.register;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.RegisterForm;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

interface RegisterView extends MvpView {

    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);

    void onRegister(User user);

    void onError(ApiError error);
}
