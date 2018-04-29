package istanbul.codify.muudy.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import java.util.List;

import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;

public final class UserProfileActivity extends MuudyActivity implements UserProfileView {

    private UserProfilePresenter mPresenter = new UserProfilePresenter();

    public static void start(@NonNull Long userId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull User user) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(user.getClass().getSimpleName(), user);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_profile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Long userId = getSerializable(Long.class);
        if (userId != null) {
            mPresenter.bind(userId);
        }

        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.bind(user);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onPostsClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.posts(user);
        }
    }

    @Override
    public void onTopsClicked() {
        // TODO Request tops
    }

    @Override
    public void onGamesClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.stars(user, Category.GAME);
        }
    }

    @Override
    public void onSeriesClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.stars(user, Category.SERIES);
        }
    }

    @Override
    public void onFilmsClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.stars(user, Category.FILM);
        }
    }

    @Override
    public void onBooksClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.stars(user, Category.BOOK);
        }
    }

    @Override
    public void onHiddenProfile() {
        // TODO Show follow requested
    }

    @Override
    public void onFollowClicked() {
        // TODO Follow user
    }

    @Override
    public void onUserFollowed() {
        // TODO Show user followed
    }

    @Override
    public void onLoadedPosts(List<Post> posts) {
        mPresenter.bindPosts(posts);
    }

    @Override
    public void onLoadedStars(List<Post> stars) {
        mPresenter.bindStars(stars);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }
}
