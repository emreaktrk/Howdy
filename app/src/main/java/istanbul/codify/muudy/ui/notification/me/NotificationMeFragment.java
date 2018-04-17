package istanbul.codify.muudy.ui.notification.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.HowdyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;

import java.util.List;

public final class NotificationMeFragment extends HowdyFragment implements NotificationMeView {

    private NotificationMePresenter mPresenter = new NotificationMePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification_me;
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
    public void onLoaded(List<Notification> notifications, List<FollowRequest> requests) {
        mPresenter.bind(notifications, requests);
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
