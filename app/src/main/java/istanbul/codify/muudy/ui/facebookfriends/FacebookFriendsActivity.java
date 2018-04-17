package istanbul.codify.muudy.ui.facebookfriends;

import android.os.Bundle;
import android.support.annotation.Nullable;

import istanbul.codify.muudy.HowdyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;

import java.util.List;

public final class FacebookFriendsActivity extends HowdyActivity implements FacebookFriendsView {

    private FacebookFriendsPresenter mPresenter = new FacebookFriendsPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_facebook_friends;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(List<User> users) {

    }

    @Override
    public void onFollowClicked(User user) {

    }

    @Override
    public void onUnfollowClicked(User user) {

    }

    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void onCloseClicked() {

    }
}
