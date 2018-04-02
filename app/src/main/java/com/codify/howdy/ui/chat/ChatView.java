package com.codify.howdy.ui.chat;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Chat;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface ChatView extends MvpView {

    void onSendClicked(String message);

    void onMediaClicked();

    void onLoaded(User user);

    void onLoaded(List<Chat> chats);

    void onLoaded(Object object);

    void onError(ApiError error);

    void onBackClicked();
}
