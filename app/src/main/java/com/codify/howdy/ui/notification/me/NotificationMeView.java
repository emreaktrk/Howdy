package com.codify.howdy.ui.notification.me;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.FollowRequest;
import com.codify.howdy.model.Notification;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface NotificationMeView extends MvpView {

    void onLoaded(List<Notification> notifications, List<FollowRequest> requests);

    void onError(ApiError error);

    void onNotificationClicked(Notification notification);
}
