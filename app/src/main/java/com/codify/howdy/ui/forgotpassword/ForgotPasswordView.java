package com.codify.howdy.ui.forgotpassword;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.ui.base.MvpView;

interface ForgotPasswordView extends MvpView {

    void onSendClicked();

    void onLoaded(String data);

    void onError(ApiError error);

    void onCloseClicked();
}
