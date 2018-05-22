package istanbul.codify.muudy.ui.followrequests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.NotificationsMe;
import istanbul.codify.muudy.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FollowRequestActivity extends MuudyActivity implements EventSupport,FollowRequestsView {

    private FollowRequestsPresenter mPresenter = new FollowRequestsPresenter();

    public static void start(@NonNull List<FollowRequest> requests) {
        Context context = Utils.getApp().getApplicationContext();

        NotificationsMe notificationsMe = new NotificationsMe((ArrayList<FollowRequest>) requests);

        Intent starter = new Intent(context, FollowRequestActivity.class);
        starter.putExtra(notificationsMe.getClass().getSimpleName(),notificationsMe);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_follow_requests;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this,this);

        NotificationsMe notificationsMe = getSerializable(NotificationsMe.class);

        mPresenter.bind(notificationsMe.followRequests);

    }


    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onLoaded(List<FollowRequest> requests) {
        mPresenter.bind(requests);
    }

    @Override
    public void onFollowRequestClicked(FollowRequest request) {
        mPresenter.getNotifications();
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
    public void onRequestAcceptOrDecline(AnswerFollowResponse answerFollowResponse) {
        mPresenter.getNotifications();
    }
}
