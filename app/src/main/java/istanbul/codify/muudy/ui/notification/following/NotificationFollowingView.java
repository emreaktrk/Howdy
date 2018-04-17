package istanbul.codify.muudy.ui.notification.following;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.model.NotificationFollowing;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface NotificationFollowingView extends MvpView {

    void onLoaded(List<NotificationFollowing> notifications);

    void onError(ApiError error);

    void onNotificationFollowingClicked(NotificationFollowing notification);
}
