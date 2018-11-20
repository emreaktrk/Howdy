package istanbul.codify.monju.ui.forgotpassword;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.ui.base.MvpView;

interface ForgotPasswordView extends MvpView {

    void onSendClicked();

    void onLoaded(String data);

    void onError(ApiError error);

    void onCloseClicked();
}
