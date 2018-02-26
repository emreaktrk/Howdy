package com.codify.howdy.ui.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;
import com.codify.howdy.ui.notification.followed.NotificationFollowedFragment;
import com.codify.howdy.ui.notification.you.NotificationYouFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public final class NotificationFragment extends NavigationFragment implements NotificationView {

    private NotificationPresenter mPresenter = new NotificationPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification;
    }

    @Override
    public int getSelection() {
        return Navigation.NOTIFICATION;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public FragmentPagerItemAdapter create() {
        return new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                FragmentPagerItems
                        .with(getContext())
                        .add("Takip Edilen", NotificationFollowedFragment.class)
                        .add("Sen", NotificationYouFragment.class)
                        .create());
    }
}
