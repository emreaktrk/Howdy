package istanbul.codify.muudy.ui.chat;

import android.net.Uri;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Chat;
import istanbul.codify.muudy.model.Result;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

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
