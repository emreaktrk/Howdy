package istanbul.codify.muudy.ui.forgotpassword;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface ForgotPasswordView extends MvpView {

    void onSendClicked();

    void onLoaded(String data);

    void onError(ApiError error);

    void onCloseClicked();
}
