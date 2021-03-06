package istanbul.codify.monju.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.account.sync.SyncListener;
import istanbul.codify.monju.analytics.Analytics;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.navigation.Navigation;
import istanbul.codify.monju.navigation.NavigationFragment;
import istanbul.codify.monju.ui.photo.PhotoActivity;
import istanbul.codify.monju.ui.postdetail.PostDetailActivity;
import istanbul.codify.monju.ui.profileedit.ProfileEditActivity;
import istanbul.codify.monju.ui.settings.SettingsActivity;
import istanbul.codify.monju.ui.userphotos.UserPhotosActivity;
import istanbul.codify.monju.ui.users.UsersActivity;
import istanbul.codify.monju.ui.video.VideoActivity;
import istanbul.codify.monju.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;


public final class ProfileFragment extends NavigationFragment implements ProfileView, SyncListener, EventSupport {

    private ProfilePresenter mPresenter = new ProfilePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_profile;
    }

    @Override
    public int getSelection() {
        return Navigation.PROFILE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, this);

        if (mPresenter.mUser != null){
            mPresenter.bind(mPresenter.mUser);
        }


            if (mPresenter.selectedIndex == 0 && mPresenter.posts != null) {
                mPresenter.bindPosts(mPresenter.posts);
            }else if (mPresenter.selectedIndex == 1 && mPresenter.userTops != null){
                mPresenter.bindUserTops(mPresenter.userTops);
            }else if (mPresenter.stars!= null){
                mPresenter.bindStars(mPresenter.stars);
            }

            mPresenter.setSelected(mPresenter.selectedIndex);

        AccountUtils.sync(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onFollowersClicked() {
        User me = AccountUtils.me(getContext());
        UsersActivity.start(me, UsersScreenMode.FOLLOWER);
    }

    @Override
    public void onFollowingsClicked() {
        User me = AccountUtils.me(getContext());
        UsersActivity.start(me, UsersScreenMode.FOLLOWING);
    }

    @Override
    public void onPictureClicked() {
        User me = AccountUtils.me(getContext());
        UserPhotosActivity.start(me);
    }

    @Override
    public void onPostsClicked() {
        mPresenter.posts();
    }

    @Override
    public void onTopsClicked() {
        mPresenter.tops();
    }

    @Override
    public void onGamesClicked() {
        mPresenter.stars(Category.GAME);
    }

    @Override
    public void onSeriesClicked() {
        mPresenter.stars(Category.SERIES);
    }

    @Override
    public void onFilmsClicked() {
        mPresenter.stars(Category.FILM);
    }

    @Override
    public void onBooksClicked() {
        mPresenter.stars(Category.BOOK);
    }

    @Override
    public void onSettingsClicked() {
        SettingsActivity.start();
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
    public void onLoadedUserTops(ArrayList<UserTop> userTops) {
        mPresenter.bindUserTops(userTops);
    }

    @Override
    public void onLoaded(List<Post> posts, int selectedIndex) {
        if (selectedIndex == 0) {
            mPresenter.bindPosts(posts);
        } else {
            mPresenter.bindStars(posts);
        }
    }

    @Override
    public void onPostClicked(Post post) {
        PostDetailActivity.start(post.idpost);
    }

    @Override
    public void onLikeClicked(Like like) {
        if (like.isChecked) {
            mPresenter.like(like.post.idpost);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.LIKE);
        } else {
            mPresenter.dislike(like.post.idpost);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.DISLIKE);
        }
    }

    @Override
    public void onVideoClicked(Post post) {
        VideoActivity.start(post);
    }

    @Override
    public void onImageClicked(Post post) {
        PhotoActivity.start(post);
    }

    @Override
    public void onDeleteClicked(Post post) {
        if (getContext() != null) {
            new AlertDialog
                    .Builder(getContext())
                    .setMessage("Silmek istiyor musunuz?")
                    .setPositiveButton("Evet", (dialogInterface, which) -> {
                        mPresenter.delete(post);

                        Analytics
                                .getInstance()
                                .custom(Analytics.Events.DELETE_POST);
                    })
                    .setNegativeButton("Hayir", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }
    }

    @Override
    public void onLikeCountClicked(Post post) {
        UsersActivity.start(UsersScreenMode.LIKERS, post.idpost);
    }


    @Override
    public void onPostDeleted() {
        AccountUtils.sync(getContext());
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onFacebookClicked() {
        User me = AccountUtils.me(getContext());
        AndroidUtils.browser("http://www.facebook.com/" + me.facebooklink);
    }

    @Override
    public void onTwitterClicked() {
        User me = AccountUtils.me(getContext());
        AndroidUtils.browser("http://www.twitter.com/" + me.twitterlink);
    }

    @Override
    public void onInstagramClicked() {
        User me = AccountUtils.me(getContext());
        AndroidUtils.browser("http://www.instagram.com/" + me.instagramlink);
    }

    @Override
    public void onEditClicked() {
        ProfileEditActivity.start();
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

    @Override
    public void onSync(User me) {
        mPresenter.bind(me);
        mPresenter.refresh();
    }
}
