package com.codify.howdy.ui.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;

public final class UserMessagesActivity extends HowdyActivity implements UserMessagesView {

    private UserMessagesPresenter mPresenter = new UserMessagesPresenter();

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

    }

    @Override
    public void onLoaded(Object data) {
        mPresenter.bind(data);
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
    }
}
