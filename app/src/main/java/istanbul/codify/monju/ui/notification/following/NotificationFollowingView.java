package istanbul.codify.monju.ui.notification.following;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.NotificationFollowing;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface NotificationFollowingView extends MvpView {

    void onLoaded(List<NotificationFollowing> notifications);

    void onError(ApiError error);

    void onNotificationFollowingClicked(NotificationFollowing notification);
}
