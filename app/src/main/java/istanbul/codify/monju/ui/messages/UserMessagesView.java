package istanbul.codify.monju.ui.messages;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.UserMessage;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface UserMessagesView extends MvpView {

    void onCloseClicked();

    void onNewClicked();

    void onUserMessageClicked(UserMessage message);

    void onUserMessagesDeleted(UserMessage userMessage);

    void onLoaded(List<UserMessage> messages);

    void onError(ApiError error);
}
