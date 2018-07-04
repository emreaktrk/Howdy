package istanbul.codify.muudy.ui.notification.me;

import android.graphics.Bitmap;

import istanbul.codify.muudy.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface NotificationMeView extends MvpView {

    void onLoaded(List<Notification> notifications, List<FollowRequest> requests);

    void onRequestAcceptOrDecline(AnswerFollowResponse answerFollowResponse);

    void onError(ApiError error);

    void onNotificationClicked(Notification notification);

    void onFollowRequestClicked(FollowRequest request);

    void onAcceptFollowRequestClicked(FollowRequest request);

    void onDeclineFollowRequestClicked(FollowRequest request);

    void onSeeAllClicked(List<FollowRequest> requests);

    void onRefresh();

    void openWeeklyTop(Notification notification, Bitmap bitmap);
}
