package com.codify.howdy.ui.messages;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.UserMessage;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface UserMessagesView extends MvpView {

    void onCloseClicked();

    void onNewClicked();

    void onUserMessageClicked(UserMessage message);

    void onLoaded(List<UserMessage> messages);

    void onError(ApiError error);
}
