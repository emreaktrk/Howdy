package istanbul.codify.muudy.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.FollowLink;
import istanbul.codify.muudy.deeplink.MessageLink;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.chat.ChatActivity;
import istanbul.codify.muudy.ui.photo.PhotoActivity;
import istanbul.codify.muudy.ui.postdetail.PostDetailActivity;
import istanbul.codify.muudy.ui.settings.SettingsActivity;
import istanbul.codify.muudy.ui.userphotos.UserPhotosActivity;
import istanbul.codify.muudy.ui.userprofile.more.MoreBottomSheet;
import istanbul.codify.muudy.ui.users.UsersActivity;
import istanbul.codify.muudy.ui.video.VideoActivity;
import istanbul.codify.muudy.utils.AndroidUtils;
import istanbul.codify.muudy.view.FollowButton;

import java.util.ArrayList;
import java.util.List;

public final class UserProfileActivity extends MuudyActivity implements UserProfileView {

    private UserProfilePresenter mPresenter = new UserProfilePresenter();

    public static void start(@NonNull Long userId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);
        ActivityUtils.startActivity(starter);
    }

    public static void startForMe(@NonNull Long userId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);
        Boolean isForMe = true;
        starter.putExtra(isForMe.getClass().getSimpleName(), isForMe);
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

        Boolean isForMe = getSerializable(Boolean.class);

        if (isForMe != null && isForMe) {
            mPresenter.hideFollowView();
        }

        Long userId = getSerializable(Long.class);
        if (userId != null) {
            mPresenter.load(userId, true);
        }

        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.load(user.iduser, true);
           // mPresenter.bind(user);
        }


        DeepLinkManager
                .getInstance()
                .nullifyIf(FollowLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onMessageClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            ChatActivity.start(user);
        }
    }

    @Override
    public void onMuudyClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            mPresenter.sayHi(user.iduser);
        }
    }

    @Override
    public void onMuudySent() {
        ToastUtils.showShort("Muudy de başarıyla yollandı!");

        Analytics
                .getInstance()
                .custom(Analytics.Events.MUUDY);
    }

    @Override
    public void onFollowersClicked() {
        User user = mPresenter.getUser();

        if (user != null && mPresenter.isVisibleProfile()) {
            UsersActivity.start(user, UsersScreenMode.FOLLOWER);
        }
    }

    @Override
    public void onFollowingsClicked() {
        User user = mPresenter.getUser();
        if (user != null && mPresenter.isVisibleProfile()) {
            UsersActivity.start(user, UsersScreenMode.FOLLOWING);
        }
    }

    @Override
    public void onMoreClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            if (user.iduser == AccountUtils.me(this).iduser){
                SettingsActivity.start();
            }else {
                MoreBottomSheet
                        .newInstance(user)
                        .setOnBlockClickListener(isBlocked -> mPresenter.block(isBlocked))
                        .setOnNotificationClickListener(isEnable -> mPresenter.notification(isEnable))
                        .setOnReportClickListener(() -> mPresenter.report())
                        .setOnUnfollowClickListener(() -> {
                            mPresenter.unfollow();
                            mPresenter.showNext(user.isfollowing);
                        })
                        .show(getSupportFragmentManager(), null);
            }
        }
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
    public void onFollowClicked(FollowButton compound) {
        switch (compound.getState()) {

            case FOLLOW:
                mPresenter.follow();
                mPresenter.showNext(mPresenter.getUser().isfollowing);
                if (mPresenter.getUser().isprofilehidden == ProfileVisibility.VISIBLE) {
                    compound.setState(FollowButton.State.UNFOLLOW);
                } else {
                    compound.setState(FollowButton.State.REQUEST_CANCEL);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case UNFOLLOW:
                mPresenter.unfollow();
                if (mPresenter.getUser().isprofilehidden == ProfileVisibility.VISIBLE) {
                    compound.setState(FollowButton.State.FOLLOW);
                } else {
                    compound.setState(FollowButton.State.REQUEST);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.UNFOLLOW);
                return;
            case REQUEST:
                mPresenter.requestFollow();
                compound.setState(FollowButton.State.REQUEST_CANCEL);

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case REQUEST_CANCEL:
                mPresenter.cancelRequestFollow();
                if (mPresenter.getUser().isprofilehidden == ProfileVisibility.VISIBLE) {
                    compound.setState(FollowButton.State.FOLLOW);
                } else {
                    compound.setState(FollowButton.State.REQUEST);
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
    public void onPictureClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            UserPhotosActivity.start(user);
        }
    }

    @Override
    public void onLoaded(User user, Boolean isForPosts) {
        mPresenter.bind(user);
        if (isForPosts) {
            mPresenter.posts();
        }
    }

    @Override
    public void onLoadedPosts(List<Post> posts) {
        mPresenter.bindPosts(posts);
    }

    @Override
    public void onLoadedUserTops(ArrayList<UserTop> userTops) {
        mPresenter.bindUserTops(userTops);
    }

    @Override
    public void onLoadedStars(List<Post> stars) {
        mPresenter.bindStars(stars);
    }

    /*@Override
    public void onLoaded(List<Post> posts, int selectedIndex) {
        if (selectedIndex == 0){
            mPresenter.bindPosts(posts);
        }else if (selectedIndex == 1){
            mPresenter.bindUserTops();
        }else{
            mPresenter.bindStars(posts);
        }
    }*/

    @Override
    public void onRefresh() {
        mPresenter.refresh();
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
    public void onMuudyClicked(Post post) {
        new AlertDialog
                .Builder(this)
                .setMessage("Muudy demek istiyor musunuz?")
                .setPositiveButton("Muudy De!", (dialogInterface, which) -> {
                    mPresenter.sayHi(post.iduser);

                    Analytics
                            .getInstance()
                            .custom(Analytics.Events.MUUDY);
                })
                .setNegativeButton("Hayir", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onImageClicked(Post post) {
        PhotoActivity.start(post);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onFacebookClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            AndroidUtils.browser("http://www.facebook.com/" + user.facebooklink);
        }
    }

    @Override
    public void onTwitterClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            AndroidUtils.browser("http://www.twitter.com/" + user.twitterlink);
        }
    }

    @Override
    public void onInstagramClicked() {
        User user = mPresenter.getUser();
        if (user != null) {
            AndroidUtils.browser("http://www.instagram.com/" + user.instagramlink);
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
