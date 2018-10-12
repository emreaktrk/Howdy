package istanbul.codify.muudy.ui.notification.following;

import android.support.v7.widget.RecyclerView;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetNotificationsFollowingRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetNotificationsFollowingResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.NotificationFollowing;
import istanbul.codify.muudy.ui.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

final class NotificationFollowingPresenter extends BasePresenter<NotificationFollowingView> {

    List<NotificationFollowing> notifications;

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
                                notifications = response.data;
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<NotificationFollowing> notifications) {
        NotificationFollowingAdapter adapter = new NotificationFollowingAdapter(notifications);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Notification following clicked");

                            mView.onNotificationFollowingClicked(cell);
                        }));

        findViewById(R.id.notification_following_recycler, RecyclerView.class).setAdapter(adapter);
    }
}
