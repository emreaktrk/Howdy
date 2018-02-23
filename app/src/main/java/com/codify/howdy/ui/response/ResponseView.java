package com.codify.howdy.ui.response;

import com.codify.howdy.ui.base.MvpView;

interface ResponseView extends MvpView {

    void onWordSelectClicked();

    void onSendClicked();

    void onCloseClicked();
}
