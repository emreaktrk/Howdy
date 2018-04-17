package istanbul.codify.muudy.ui.notification.me;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface NotificationMeView extends MvpView {

    void onLoaded(List<Notification> notifications, List<FollowRequest> requests);

    void onError(ApiError error);

    void onNotificationClicked(Notification notification);
}
