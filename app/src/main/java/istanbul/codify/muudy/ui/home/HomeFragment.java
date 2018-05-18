package istanbul.codify.muudy.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
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
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.DeleteEvent;
import istanbul.codify.muudy.model.event.PostEvent;
import istanbul.codify.muudy.model.event.notification.NotificationEvent;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.navigation.Navigator;
import istanbul.codify.muudy.ui.around.AroundActivity;
import istanbul.codify.muudy.ui.main.MainActivity;
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
                            mPresenter.getWall(getContext());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Empty block
                    }
                })
                .check();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationEvent event) {
        if (getNotificationActionType(event.message) == NotificationActionType.MESSAGE) {
           mPresenter.bind(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
*/
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSearchClicked() {
        UserSearchActivity.start(ResultTo.FRAGMENT);
    }

    @Override
    public void onMessagesClicked() {
        mPresenter.bind(false);
        UserMessagesActivity.start();
    }

    @Override
    public void onLoaded(Wall wall) {
        mPresenter.bind(wall);
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
        UserProfileActivity.start(user);
    }

    @Override
    public void onFollowClicked(Follow follow) {
        // TODO Follow

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
        mPresenter.getWall(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostEvent(PostEvent event) {
        mPresenter.getWall(getContext());

        ArrayList<AroundUsers> around = event.newPost.aroundUsers;
        if (!around.isEmpty()) {
            AroundActivity.start(around);
        }
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
        mPresenter.getWall(getContext());
    }

    @Override
    public int getSelection() {
        return Navigation.HOME;
    }


}
