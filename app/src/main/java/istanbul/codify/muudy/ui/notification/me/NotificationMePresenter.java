package istanbul.codify.muudy.ui.notification.me;

import android.support.v7.widget.RecyclerView;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetNotificationsMeRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetNotificationsMeResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.notification.NotificationAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

final class NotificationMePresenter extends BasePresenter<NotificationMeView> {

    void getNotifications() {
        GetNotificationsMeRequest request = new GetNotificationsMeRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getNotificationsMe(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetNotificationsMeResponse>() {
                            @Override
                            protected void success(GetNotificationsMeResponse response) {
                                mView.onLoaded(response.data.userNotifications, response.data.followRequests);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<Notification> notifications, List<FollowRequest> requests) {
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Notification clicked");

                            mView.onNotificationClicked(cell);
                        }));

        findViewById(R.id.notification_me_notification_recycler, RecyclerView.class).setAdapter(adapter);
    }
}
