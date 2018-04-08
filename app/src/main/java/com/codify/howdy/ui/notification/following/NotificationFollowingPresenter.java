package com.codify.howdy.ui.notification.following;

import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetNotificationsFollowingRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetNotificationsFollowingResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.NotificationsFollowing;
import com.codify.howdy.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

final class NotificationFollowingPresenter extends BasePresenter<NotificationFollowingView> {

    void getNotifications() {
        GetNotificationsFollowingRequest request = new GetNotificationsFollowingRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getNotificationsFollowing(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetNotificationsFollowingResponse>() {
                            @Override
                            protected void success(GetNotificationsFollowingResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<NotificationsFollowing> notifications) {

    }
}
