package istanbul.codify.monju.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.DeepLinkManager;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.model.event.notification.MessageNotificationEvent;
import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.ui.chat.ChatActivity;
import istanbul.codify.monju.ui.search.UserSearchActivity;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public final class UserMessagesActivity extends MuudyActivity implements UserMessagesView, EventSupport {

    private UserMessagesPresenter mPresenter = new UserMessagesPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserMessagesActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_messages;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getMessages();

        DeepLink pending = DeepLinkManager
                .getInstance()
                .getPending();

        if (pending != null) {
            pending.navigate(this);
        }

    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onNewClicked() {
        Long userId = AccountUtils.me(this).iduser;
        if (userId != null) {
            UserSearchActivity.startWithFollowedUsers(ResultTo.ACTIVITY, userId);
        }
    }

    @Override
    public void onUserMessageClicked(UserMessage message) {
        ChatActivity.start(message.message_touserid);
    }

    @Override
    public void onUserMessagesDeleted(UserMessage userMessage) {
        mPresenter.deleteUserMessage(userMessage);
    }

    @Override
    public void onLoaded(List<UserMessage> messages) {
        mPresenter.bind(messages);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageNotificationEvent event) {
        mPresenter.getMessages();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User user = resolveResult(requestCode, resultCode, data, User.class, UserSearchActivity.REQUEST_CODE);
        if (user != null) {
            ChatActivity.start(user);
        }
    }
}
