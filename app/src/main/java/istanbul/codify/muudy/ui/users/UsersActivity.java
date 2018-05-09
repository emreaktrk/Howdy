package istanbul.codify.muudy.ui.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Emotion;
import istanbul.codify.muudy.model.Follow;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;

import java.util.List;

public final class UsersActivity extends MuudyActivity implements UsersView {

    private UsersPresenter mPresenter = new UsersPresenter();

    public static void start(@Nullable Emotion emotion) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        if (emotion != null) {
            starter.putExtra(emotion.getClass().getSimpleName(), emotion);
        }
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
        // TODO Follow

        if (follow.isFollowed) {
            Analytics
                    .getInstance()
                    .custom(Analytics.Events.FOLLOW);
        } else {
            Analytics
                    .getInstance()
                    .custom(Analytics.Events.UNFOLLOW);
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
