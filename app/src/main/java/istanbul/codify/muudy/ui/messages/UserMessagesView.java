package istanbul.codify.muudy.ui.messages;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.UserMessage;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface UserMessagesView extends MvpView {

    void onCloseClicked();

    void onNewClicked();

    void onUserMessageClicked(UserMessage message);

    void onUserMessagesDeleted(UserMessage userMessage);

    void onLoaded(List<UserMessage> messages);

    void onError(ApiError error);
}
