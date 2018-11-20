package istanbul.codify.monju.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.analytics.Analytics;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.deeplink.DeepLinkManager;
import istanbul.codify.monju.deeplink.MessageLink;
import istanbul.codify.monju.model.Chat;
import istanbul.codify.monju.model.NotificationActionType;
import istanbul.codify.monju.model.Result;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.event.notification.MessageNotificationEvent;
import istanbul.codify.monju.model.event.notification.MessageReadedNotificationEvent;
import istanbul.codify.monju.ui.media.MediaBottomSheet;
import istanbul.codify.monju.ui.photo.PhotoActivity;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public final class ChatActivity extends MuudyActivity implements ChatView, EventSupport {

    private ChatPresenter mPresenter = new ChatPresenter();

    public static void start(@NonNull User user) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ChatActivity.class);
        starter.putExtra(user.getClass().getSimpleName(), user);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull Long userId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ChatActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_chat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.bind(user);
            mPresenter.getMessages(user.iduser);
            return;
        }

        Long userId = getSerializable(Long.class);
        if (userId != null) {
            mPresenter.getUser(userId);
        }

        DeepLinkManager
                .getInstance()
                .nullifyIf(MessageLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked(String message) {
        mPresenter.send(message);

        Analytics
                .getInstance()
                .custom(Analytics.Events.CHAT_MESSAGE);
    }

    @Override
    public void onMediaClicked() {
        MediaBottomSheet
                .newInstance()
                .setOnCameraClickListener(() -> mPresenter.capturePhoto(ChatActivity.this))
                .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ChatActivity.this))
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPhotoSelected(Uri uri) {
        mPresenter.send(uri);
    }

    @Override
    public void onLoaded(User user) {
        mPresenter.bind(user);
        mPresenter.getMessages(user.iduser);
    }

    @Override
    public void onLoaded(List<Chat> chats) {
        mPresenter.bind(chats);
    }

    @Override
    public void onMessageSent(Result result) {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.bind(user);
            mPresenter.getMessages(user.iduser);
            return;
        }

        Long userId = getSerializable(Long.class);
        if (userId != null) {
            mPresenter.getUser(userId);
        }
    }

    @Override
    public void onImageClicked(Chat chat) {
        PhotoActivity.start(chat.message_img_path);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageNotificationEvent event) {
        User user = getSerializable(User.class);
        if (getNotificationActionType(event.message) == NotificationActionType.MESSAGE) {
            if (user != null) {
                mPresenter.bind(user);
                mPresenter.getMessages(user.iduser);
                return;
            }

            Long userId = getSerializable(Long.class);
            if (userId != null) {
                mPresenter.getUser(userId);
            }
        } else if (getNotificationActionType(event.message) == NotificationActionType.MESSAGE_READED) {
            if (user != null) {
                mPresenter.bind(user);
                mPresenter.getMessages(user.iduser);
                return;
            }

            Long userId = getSerializable(Long.class);
            if (userId != null) {
                mPresenter.getUser(userId);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageReadedNotificationEvent event) {
        User user = mPresenter.getmUser();
            if (user != null) {
                mPresenter.bind(user);
                mPresenter.getMessages(user.iduser);
                return;
            }
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Throwable throwable) {
        ToastUtils.showShort(throwable.getMessage());
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onProfileClicked(long userId) {
        UserProfileActivity.start(userId);
    }

    public Long getUserId() {
        User user = getSerializable(User.class);
        if (user != null) {
            return user.iduser;
        }

        return getSerializable(Long.class);
    }
}
