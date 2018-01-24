package com.codify.howdy.ui.register;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.RegisterForm;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

interface RegisterView extends MvpView {

    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);

    void onRegister(User user);

    void onError(ApiError error);
}
