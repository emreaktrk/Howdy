package istanbul.codify.muudy.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.helper.BlurBuilder;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.DeleteEvent;
import istanbul.codify.muudy.model.event.HomeReselectEvent;
import istanbul.codify.muudy.model.event.PostEvent;
import istanbul.codify.muudy.model.event.notification.MessageNotificationEvent;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.navigation.Navigator;
import istanbul.codify.muudy.ui.around.AroundActivity;
import istanbul.codify.muudy.ui.chat.ChatActivity;
import istanbul.codify.muudy.ui.messages.UserMessagesActivity;
import istanbul.codify.muudy.ui.photo.PhotoActivity;
import istanbul.codify.muudy.ui.postdetail.PostDetailActivity;
import istanbul.codify.muudy.ui.search.UserSearchActivity;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.ui.users.UsersActivity;
import istanbul.codify.muudy.ui.video.VideoActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public final class HomeFragment extends NavigationFragment implements HomeView, EventSupport {

    private HomePresenter mPresenter = new HomePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        Dexter
                .withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            ArrayList<AroundUsers> around = (ArrayList<AroundUsers>)getSerializable(ArrayList.class);

                            mPresenter.getWall(getContext(), null,around);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Empty block
                    }
                })
                .check();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageNotificationEvent event) {
        Activity activity = ActivityUtils.getTopActivity();
        mPresenter.showNewMessageDot(true);

        if (activity instanceof UserMessagesActivity) {
            mPresenter.showNewMessageDot(false);
        }

        if (activity instanceof ChatActivity) {
            Long activeUserId = ((ChatActivity) activity).getUserId();
            if (activeUserId != null) {
                Long eventUserId = event.getUserId();
                if (eventUserId != null) {
                    if (activeUserId.equals(eventUserId)) {
                        mPresenter.showNewMessageDot(false);
                    }
                }
            }
        }
    }

    @Override
    public void onSearchClicked() {
        UserSearchActivity.start(ResultTo.FRAGMENT);
    }

    @Override
    public void onMessagesClicked() {
        mPresenter.showNewMessageDot(false);

        UserMessagesActivity.start();
    }

    @Override
    public void onLoaded(Wall wall) {
        mPresenter.bind(wall,null);
    }

    @Override
    public void onLoaded(Wall wall, ArrayList<AroundUsers> aroundUsers) {
        mPresenter.bind(wall,aroundUsers);
        //mPresenter.takeBlurredImage(aroundUsers);
    }

    @Override
    public void onPostsLoaded(ArrayList<AroundUsers> aroundUsers) {
        mPresenter.takeBlurredImage(aroundUsers);
    }

    @Override
    public void onMoreLoaded(ArrayList<Post> posts, More more) {
        if (posts == null || posts.isEmpty()) {
            more.enable = false;
        }

        more.page = more.page + 1;
        mPresenter.add(posts, more);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onEmotionClicked(Emotion emotion) {
        UsersActivity.start(emotion);
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
    public void onAvatarClicked(Post post) {
        boolean own = AccountUtils.own(getContext(), post.iduser);
        if (own) {
            Navigator
                    .with(this)
                    .setSelected(Navigation.PROFILE);
        } else {
            UserProfileActivity.start(post.iduser);
        }
    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }

    @Override
    public void onFollowClicked(Follow follow) {
        switch (follow.mCompound.getState()) {
            case FOLLOW:
                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case UNFOLLOW:
                Analytics
                        .getInstance()
                        .custom(Analytics.Events.UNFOLLOW);
                return;
        }
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
    public void onMuudyClicked(Post post) {
        if (getContext() != null) {
            new AlertDialog
                    .Builder(getContext())
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
    }

    @Override
    public void onPostDeleted() {
        EventBus
                .getDefault()
                .post(new DeleteEvent());
    }

    @Override
    public void onRefresh() {
        mPresenter.getWall(getContext(), null,null);
    }

    @Override
    public void onMorePage(More more) {
        mPresenter.getWall(getContext(), more,null);
    }

    @Override
    public void onLikeCountClicked(Post post) {
        UsersActivity.start(UsersScreenMode.LIKERS, post.idpost);
    }

    @Override
    public void onBlurredImageTaken(ArrayList<AroundUsers> arounds, Bitmap bitmap) {

        AroundActivity.start(arounds, bitmap);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostEvent(PostEvent event) {
        mPresenter.getWall(getContext(), null,null);
        ArrayList<AroundUsers> around = event.newPost.aroundUsers;
        if (!around.isEmpty()) {
            mPresenter.takeBlurredImage(around);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeReselectEvent(HomeReselectEvent event) {
        mPresenter.scrollToTop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User user = resolveResult(requestCode, resultCode, data, User.class, UserSearchActivity.REQUEST_CODE);
        if (user != null) {
            UserProfileActivity.start(user.iduser);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteEvent(DeleteEvent event) {
        mPresenter.getWall(getContext(), null,null);
    }

    @Override
    public int getSelection() {
        return Navigation.HOME;
    }

}
