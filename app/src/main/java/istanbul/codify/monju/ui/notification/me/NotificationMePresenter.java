package istanbul.codify.monju.ui.notification.me;

import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.AnswerFollowRequest;
import istanbul.codify.monju.api.pojo.request.GetNotificationsMeRequest;
import istanbul.codify.monju.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetNotificationsMeResponse;
import istanbul.codify.monju.helper.BlurBuilder;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.FollowRequest;
import istanbul.codify.monju.model.FollowResponse;
import istanbul.codify.monju.model.Notification;
import istanbul.codify.monju.ui.base.BasePresenter;
import istanbul.codify.monju.ui.notification.NotificationAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

final class  NotificationMePresenter extends BasePresenter<NotificationMeView> {

    List<Notification> notifications;
    List<FollowRequest> requests;
    void getNotifications() {
        GetNotificationsMeRequest request = new GetNotificationsMeRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        findViewById(R.id.notification_refresh, SwipeRefreshLayout.class).setRefreshing(true);
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getNotificationsMe(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetNotificationsMeResponse>() {
                            @Override
                            protected void success(GetNotificationsMeResponse response) {
                                findViewById(R.id.notification_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                notifications = response.data.userNotifications;
                                requests = response.data.followRequests;
                                mView.onLoaded(response.data.userNotifications, response.data.followRequests);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                findViewById(R.id.notification_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<Notification> notifications, List<FollowRequest> requests) {
        NotificationAdapter adapter = new NotificationAdapter(notifications,requests);
        mDisposables.add(
                adapter
                        .notificationClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Notification clicked");
                            mView.onNotificationClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .followRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onFollowRequestClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .acceptFollowRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onAcceptFollowRequestClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .declineFollowRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onDeclineFollowRequestClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .seeAllRequestsClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onSeeAllClicked(cell);
                        }));

        mDisposables.add(
                RxSwipeRefreshLayout
                        .refreshes(findViewById(R.id.notification_refresh))
                        .subscribe(o -> {
                            Logcat.v("Refresh clicked");

                            mView.onRefresh();
                        }));


        findViewById(R.id.notification_me_notification_recycler, RecyclerView.class).setItemAnimator(null);

        findViewById(R.id.notification_me_notification_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void takeScreenShot(Notification notification){
        View content = findViewById(R.id.notification_refresh).getRootView();

        Bitmap bitmap = BlurBuilder.blur(content);
        mView.openWeeklyTop(notification,bitmap);
    }

    void acceptFollowRequest(FollowRequest followRequest){
        AnswerFollowRequest request = new AnswerFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = followRequest.user.iduser;
        request.answer = FollowResponse.ACCEPT;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerFollowResponse>() {
                            @Override
                            protected void success(AnswerFollowResponse response) {
                                mView.onRequestAcceptOrDecline(response);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void declineFollowRequest(FollowRequest followRequest){
        AnswerFollowRequest request = new AnswerFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = followRequest.user.iduser;
        request.answer = FollowResponse.DELETE;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerFollowResponse>() {
                            @Override
                            protected void success(AnswerFollowResponse response) {
                                mView.onRequestAcceptOrDecline(response);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
