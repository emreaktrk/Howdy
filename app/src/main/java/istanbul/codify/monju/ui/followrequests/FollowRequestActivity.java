package istanbul.codify.monju.ui.followrequests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.FollowRequest;
import istanbul.codify.monju.model.NotificationsMe;

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
