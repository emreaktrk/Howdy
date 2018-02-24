package com.codify.howdy.ui.main;

import android.support.v4.widget.Space;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class MainPresenter extends BasePresenter<MainView> {

    @Override
    public void attachView(MainView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_home))
                        .filter(o -> !findViewById(R.id.navigation_home).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Home clicked");
                            view.onHomeClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_statistic))
                        .filter(o -> !findViewById(R.id.navigation_statistic).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Statistic clicked");
                            view.onStatisticClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_notification))
                        .filter(o -> !findViewById(R.id.navigation_notification).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Notification clicked");
                            view.onNotificationClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_profile))
                        .filter(o -> !findViewById(R.id.navigation_profile).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Profile clicked");
                            view.onProfileClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_compose))
                        .subscribe(o -> {
                            Logcat.v("Compose clicked");
                            view.onComposeClicked();
                        }));
    }

    void setSelected(@Navigation int navigation) {
        LinearLayoutCompat bar = findViewById(R.id.navigation_bar);
        for (int position = 0; position < bar.getChildCount(); position++) {
            View child = bar.getChildAt(position);
            if (child instanceof Space || child.getId() == R.id.navigation_compose) {
                continue;
            }

            child.setSelected(child.getId() == navigation);
        }
    }
}
