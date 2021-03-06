package istanbul.codify.monju.ui.userprofile.more;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.monju.api.pojo.request.SaveSendNotificationOnPostRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.NotificationOnPostResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.FollowState;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.BasePresenter;

import java.util.concurrent.TimeUnit;

final class MorePresenter extends BasePresenter<MoreView> {

    @Override
    public void attachView(MoreView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.more_dialog_unfollow))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Unfollow clicked");

                            view.onUnfollowClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.more_dialog_report))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Report clicked");

                            view.onReportClicked();
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.more_dialog_block))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(block -> {
                            Logcat.v("Block user changed");

                            view.onBlockChanged(block);
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.more_dialog_notification))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(notification -> {
                            Logcat.v("Notification changed");

                            view.onNotificationChanged(notification);
                        }));
    }

    void bind(User user) {
        findViewById(R.id.more_dialog_unfollow).setVisibility(user.isfollowing == FollowState.FOLLOWING ? View.VISIBLE : View.GONE);
        findViewById(R.id.more_dialog_notification).setVisibility(user.isfollowing == FollowState.FOLLOWING ? View.VISIBLE : View.GONE);
        findViewById(R.id.more_dialog_block, SwitchCompat.class).setChecked(user.isbanned == 1);
        checkNotificationOnPost(user);
    }

    void checkNotificationOnPost(User user){

        SaveSendNotificationOnPostRequest request = new SaveSendNotificationOnPostRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.otherUserId = user.iduser;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .checkNotificationOnPost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<NotificationOnPostResponse>() {
                            @Override
                            protected void success(NotificationOnPostResponse response) {
                                    if (response.data.isNotificationOnPostRegistered){
                                        findViewById(R.id.more_dialog_notification, SwitchCompat.class).setChecked(true);
                                    }else{
                                        findViewById(R.id.more_dialog_notification, SwitchCompat.class).setChecked(false);
                                    }
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);


                            }
                        }));
    }
}
