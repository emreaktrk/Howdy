package istanbul.codify.muudy.ui.RegisterLastStep;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.RegisterForm;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

/**
 * Created by egesert on 31.08.2018.
 */

interface RegisterLastStepView extends MvpView {
    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);

    void onRegister(User user);

    void onError(ApiError error);
}
