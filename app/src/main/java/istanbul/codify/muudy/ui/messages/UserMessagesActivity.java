package istanbul.codify.muudy.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ResultTo;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.UserMessage;
import istanbul.codify.muudy.model.event.notification.MessageNotificationEvent;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.ui.chat.ChatActivity;
import istanbul.codify.muudy.ui.search.UserSearchActivity;
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
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onNewClicked() {
        UserSearchActivity.start(ResultTo.ACTIVITY);
    }

    @Override
    public void onUserMessageClicked(UserMessage message) {
        ChatActivity.start(message.message_touserid);
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
