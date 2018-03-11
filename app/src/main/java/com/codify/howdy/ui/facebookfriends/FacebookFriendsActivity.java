package com.codify.howdy.ui.facebookfriends;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;

import java.util.List;

public final class FacebookFriendsActivity extends HowdyActivity implements FacebookFriendsView {

    private FacebookFriendsPresenter mPresenter = new FacebookFriendsPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_facebook_friends;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(List<User> users) {

    }

    @Override
    public void onFollowClicked(User user) {

    }

    @Override
    public void onUnfollowClicked(User user) {

    }

    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void onCloseClicked() {

    }
}
