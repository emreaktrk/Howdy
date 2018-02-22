package com.codify.howdy.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Chat;
import com.codify.howdy.model.User;

import java.util.List;

public final class ChatActivity extends HowdyActivity implements ChatView {

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked() {

    }

    @Override
    public void onMediaClicked() {

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
    public void onError(ApiError error) {

    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
