package com.codify.howdy.ui.notification.following;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.FollowRequest;
import com.codify.howdy.model.Notification;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface NotificationFollowingView extends MvpView {

    void onLoaded(List<Notification> notifications);

    void onError(ApiError error);
}
