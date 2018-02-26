package com.codify.howdy.ui.notification;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.ui.base.BasePresenter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

final class NotificationPresenter extends BasePresenter<NotificationView> {

    @Override
    public void attachView(NotificationView view, View root) {
        super.attachView(view, root);

        ViewPager pager = findViewById(R.id.notification_pager, ViewPager.class);
        pager.setAdapter(view.create());
        findViewById(R.id.notification_tab, SmartTabLayout.class).setViewPager(pager);
    }
}
