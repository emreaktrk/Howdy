package istanbul.codify.monju.ui.chat;

import android.net.Uri;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Chat;
import istanbul.codify.monju.model.Result;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface ChatView extends MvpView {

    void onSendClicked(String message);

    void onMediaClicked();

    void onPhotoSelected(Uri uri);

    void onLoaded(User user);

    void onLoaded(List<Chat> chats);

    void onMessageSent(Result result);

    void onImageClicked(Chat chat);

    void onError(ApiError error);

    void onError(Throwable throwable);

    void onBackClicked();

    void onProfileClicked(long userId);

}
