package istanbul.codify.muudy.ui.notification.following;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.NotificationFollowing;
import istanbul.codify.muudy.ui.posts.PostsActivity;
import istanbul.codify.muudy.ui.users.UsersActivity;

import java.util.List;

public final class NotificationFollowingFragment extends MuudyFragment implements NotificationFollowingView {

    private NotificationFollowingPresenter mPresenter = new NotificationFollowingPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification_following;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, this);
        mPresenter.getNotifications();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(List<NotificationFollowing> notifications) {
        mPresenter.bind(notifications);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onNotificationFollowingClicked(NotificationFollowing notification) {
        switch (notification.notification_actiontype) {
            case FOLLOW:
                UsersActivity.start(notification.followedUsers);
                return;
            case LIKE:
                PostsActivity.start(notification.likedPosts);
                return;
        }
    }
}
