package com.codify.howdy.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.ResultTo;
import com.codify.howdy.model.User;
import com.codify.howdy.model.UserMessage;
import com.codify.howdy.ui.chat.ChatActivity;
import com.codify.howdy.ui.search.UserSearchActivity;

import java.util.List;

public final class UserMessagesActivity extends HowdyActivity implements UserMessagesView {

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

    @Override
    public void onError(ApiError error) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
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
