package istanbul.codify.muudy.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.event.SyncEvent;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.ui.profileedit.ProfileEditActivity;


public final class ProfileFragment extends NavigationFragment implements ProfileView, EventSupport {

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
    public void onEditClicked() {
        ProfileEditActivity.start();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(SyncEvent event) {
        User me = AccountUtils.me(getContext());
        mPresenter.bind(me);
        mPresenter.posts();
    }
}
