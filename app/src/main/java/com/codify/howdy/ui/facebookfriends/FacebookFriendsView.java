package com.codify.howdy.ui.facebookfriends;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface FacebookFriendsView extends MvpView {

    void onLoaded(List<User> users);

    void onFollowClicked(User user);

    void onUnfollowClicked(User user);

    void onError(ApiError error);

    void onCloseClicked();
}
