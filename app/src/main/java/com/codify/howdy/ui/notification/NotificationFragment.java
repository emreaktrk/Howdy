package com.codify.howdy.ui.notification;

import com.codify.howdy.R;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;

public final class NotificationFragment extends NavigationFragment implements NotificationView {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification;
    }

    @Override
    public int getSelection() {
        return Navigation.NOTIFICATION;
    }
}
