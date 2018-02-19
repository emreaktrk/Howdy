package com.codify.howdy.ui.messages;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.ui.base.MvpView;

interface UserMessagesView extends MvpView {

    void onCloseClicked();

    void onNewClicked();

    void onLoaded(Object data);

    void onError(ApiError error);
}
