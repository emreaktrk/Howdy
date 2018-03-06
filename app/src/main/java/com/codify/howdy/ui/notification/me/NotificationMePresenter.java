package com.codify.howdy.ui.notification.me;

import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetNotificationsRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetNotificationsResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.NotificationType;
import com.codify.howdy.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class NotificationMePresenter extends BasePresenter<NotificationMeView> {

    void getNotifications() {
        GetNotificationsRequest request = new GetNotificationsRequest(NotificationType.ME);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getNotifications(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetNotificationsResponse>() {
                            @Override
                            protected void success(GetNotificationsResponse response) {
                                mView.onLoaded(response.data.userNotifications, response.data.followRequests);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
