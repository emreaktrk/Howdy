package istanbul.codify.muudy.ui.notification.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.ui.weeklytopuser.WeeklyTopUsersActivity;
import istanbul.codify.muudy.ui.followrequests.FollowRequestActivity;
import istanbul.codify.muudy.ui.givevote.GiveVoteDialog;
import istanbul.codify.muudy.ui.postdetail.PostDetailActivity;
import istanbul.codify.muudy.ui.response.ResponseActivity;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;

import java.util.List;

public final class NotificationMeFragment extends MuudyFragment implements NotificationMeView {

    private NotificationMePresenter mPresenter = new NotificationMePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification_me;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*mPresenter.attachView(this, this);
        mPresenter.getNotifications();*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.attachView(this, this);
        mPresenter.getNotifications();
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
                ResponseActivity.start(notification,false);
                return;
            case ANSWER_HI:
                ResponseActivity.start(notification,true);
                return;
            case COMMENT:
                PostDetailActivity.start(notification.notification_postid);
                return;
            case GIVE_VOTE:
                GiveVoteDialog.newInstance(notification.notification_msg,notification.notification_postid).show(getFragmentManager(),null);
                return;
            case TAG:
                PostDetailActivity.start(notification.notification_postid);
                return;
            case WEEK_TOP_USERS:
                WeeklyTopUsersActivity.start();
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

}
