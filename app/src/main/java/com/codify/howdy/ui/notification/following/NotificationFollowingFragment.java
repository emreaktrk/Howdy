package com.codify.howdy.ui.notification.following;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Notification;
import com.codify.howdy.model.NotificationsFollowing;

import java.util.List;

public final class NotificationFollowingFragment extends HowdyFragment implements NotificationFollowingView {

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
    public void onLoaded(List<NotificationsFollowing> notifications) {
        mPresenter.bind(notifications);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onNotificationClicked(Notification notification) {
        // TODO Deep link
    }
}
