package istanbul.codify.monju.ui.RegisterLastStep;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.RegisterForm;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

/**
 * Created by egesert on 31.08.2018.
 */

interface RegisterLastStepView extends MvpView {
    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);

    void onRegister(User user);

    void onError(ApiError error);
}
