package istanbul.codify.monju.ui.notification.me;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.FollowRequest;
import istanbul.codify.monju.model.Notification;
import istanbul.codify.monju.ui.followrequests.FollowRequestActivity;
import istanbul.codify.monju.ui.givevote.GiveVoteDialog;
import istanbul.codify.monju.ui.postdetail.PostDetailActivity;
import istanbul.codify.monju.ui.response.ResponseActivity;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import istanbul.codify.monju.ui.weeklytopuser.WeeklyTopUsersActivity;

import java.util.List;

public final class NotificationMeFragment extends MuudyFragment implements NotificationMeView {

    private NotificationMePresenter mPresenter = new NotificationMePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification_me;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.attachView(this, this);
        if (mPresenter.notifications != null){
            mPresenter.bind(mPresenter.notifications,mPresenter.requests);
        }
        mPresenter.getNotifications();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(List<Notification> notifications, List<FollowRequest> requests) {
        mPresenter.bind(notifications, requests);
    }

    @Override
    public void onRequestAcceptOrDecline(AnswerFollowResponse answerFollowResponse) {
        mPresenter.getNotifications();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onNotificationClicked(Notification notification) {
        switch (notification.notification_actiontype) {
            case FOLLOW:
                UserProfileActivity.start(notification.notification_fromuserid);
                return;
            case LIKE:
                PostDetailActivity.start(notification.notification_postid);
                return;
            case SAY_HI:
                ResponseActivity.start(notification, false);
                return;
            case ANSWER_HI:
                ResponseActivity.start(notification, true);
                return;
            case COMMENT:
                PostDetailActivity.start(notification.notification_postid);
                return;
            case GIVE_VOTE:
                GiveVoteDialog.newInstance(notification.notification_msg, notification.notification_postid).show(getFragmentManager(), null);
                return;
            case TAG:
                PostDetailActivity.start(notification.notification_postid);
                return;
            case WEEK_TOP_USERS:
                mPresenter.takeScreenShot(notification);
            default:
                return;
        }
    }

    @Override
    public void onFollowRequestClicked(FollowRequest request) {
        UserProfileActivity.start(request.user);
    }

    @Override
    public void onAcceptFollowRequestClicked(FollowRequest request) {
        mPresenter.acceptFollowRequest(request);
    }

    @Override
    public void onDeclineFollowRequestClicked(FollowRequest request) {
        mPresenter.declineFollowRequest(request);
    }

    @Override
    public void onSeeAllClicked(List<FollowRequest> requests) {
        FollowRequestActivity.start(requests);
    }

    @Override
    public void onRefresh() {
        mPresenter.getNotifications();
    }

    @Override
    public void openWeeklyTop(Notification notification, Bitmap bitmap) {

        WeeklyTopUsersActivity.start(bitmap);
    }

}
