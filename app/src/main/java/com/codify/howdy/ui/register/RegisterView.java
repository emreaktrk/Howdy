package com.codify.howdy.ui.register;

import com.codify.howdy.model.RegisterForm;
import com.codify.howdy.ui.base.MvpView;

interface RegisterView extends MvpView {

    void onCloseClicked();

    void onRegisterClicked(RegisterForm form);
}
