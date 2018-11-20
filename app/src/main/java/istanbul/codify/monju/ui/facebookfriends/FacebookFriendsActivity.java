package istanbul.codify.monju.ui.facebookfriends;

import android.os.Bundle;
import android.support.annotation.Nullable;

import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;

import java.util.List;

public final class FacebookFriendsActivity extends MuudyActivity implements FacebookFriendsView {

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
