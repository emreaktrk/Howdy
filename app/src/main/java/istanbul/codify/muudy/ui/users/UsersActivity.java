package istanbul.codify.muudy.ui.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.view.FollowButton;

import java.util.List;

public final class UsersActivity extends MuudyActivity implements UsersView {

    private UsersPresenter mPresenter = new UsersPresenter();

    public static void start(@Nullable Emotion emotion) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        if (emotion != null) {
            starter.putExtra(emotion.getClass().getSimpleName(), emotion);
            starter.putExtra(String.class.getSimpleName(), UsersScreenMode.AROUND_WITH_EMOTION);
        }
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull User user, @NonNull @UsersScreenMode String mode) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), mode);
        starter.putExtra(User.class.getSimpleName(), user);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_users;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
        String mode = getSerializable(String.class);
        User user = getSerializable(User.class);
        mPresenter.bind(user, mode);

        Emotion emotion = getSerializable(Emotion.class);
        if (emotion != null) {
            mPresenter.aroundUsers(emotion);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onUserSearched(String query) {

    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user);
    }

    @Override
    public void onFollowClicked(Follow follow) {
        switch (follow.mCompound.getState()) {
            case FOLLOW:
                mPresenter.follow(follow.mUser);
                if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                    follow.mCompound.setState(FollowButton.State.UNFOLLOW);
                } else {
                    follow.mCompound.setState(FollowButton.State.REQUEST_CANCEL);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case UNFOLLOW:
                mPresenter.unfollow(follow.mUser);
                if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                    follow.mCompound.setState(FollowButton.State.FOLLOW);
                } else {
                    follow.mCompound.setState(FollowButton.State.REQUEST);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.UNFOLLOW);
                return;
            case REQUEST:
                mPresenter.requestFollow(follow.mUser);
                follow.mCompound.setState(FollowButton.State.REQUEST_CANCEL);

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case REQUEST_CANCEL:
                mPresenter.cancelRequestFollow(follow.mUser);
                if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                    follow.mCompound.setState(FollowButton.State.FOLLOW);
                } else {
                    follow.mCompound.setState(FollowButton.State.REQUEST);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.UNFOLLOW);
                return;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onLoaded(List<User> users) {
        mPresenter.bind(users);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }


}
