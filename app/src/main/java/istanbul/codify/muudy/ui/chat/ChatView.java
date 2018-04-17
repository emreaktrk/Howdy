package istanbul.codify.muudy.ui.chat;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Chat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

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
