package istanbul.codify.muudy.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.account.sync.SyncListener;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.UsersScreenMode;
import istanbul.codify.muudy.model.event.DeleteEvent;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.ui.photo.PhotoActivity;
import istanbul.codify.muudy.ui.postdetail.PostDetailActivity;
import istanbul.codify.muudy.ui.settings.SettingsActivity;
import istanbul.codify.muudy.ui.userphotos.UserPhotosActivity;
import istanbul.codify.muudy.ui.users.UsersActivity;
import istanbul.codify.muudy.ui.video.VideoActivity;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        //TODO Show tops in list
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
    public void onPostDeleted() {

    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onFacebookClicked() {

    }

    @Override
    public void onTwitterClicked() {

    }

    @Override
    public void onInstagramClicked() {

    }

    @Override
    public void onSync(User me) {
        mPresenter.bind(me);
        mPresenter.posts();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteEvent(DeleteEvent event) {
        if (mPresenter.getSelected() == 0) {
            mPresenter.posts();
        }
    }
}
