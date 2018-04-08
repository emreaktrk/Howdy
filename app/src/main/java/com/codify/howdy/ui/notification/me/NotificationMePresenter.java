package com.codify.howdy.ui.notification.me;

import android.support.v7.widget.RecyclerView;
import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetNotificationsMeRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetNotificationsMeResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.FollowRequest;
import com.codify.howdy.model.Notification;
import com.codify.howdy.ui.base.BasePresenter;
import com.codify.howdy.ui.notification.NotificationAdapter;
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
