package com.codify.howdy.navigation;

import android.support.annotation.IntDef;

import com.codify.howdy.R;

@IntDef({Navigation.HOME, Navigation.STATISTIC, Navigation.NOTIFICATION, Navigation.PROFILE})
public @interface Navigation {
    int HOME = R.id.navigation_home;
    int STATISTIC = R.id.navigation_statistic;
    int NOTIFICATION = R.id.navigation_notification;
    int PROFILE = R.id.navigation_profile;

    interface ISelectable {
        @Navigation
        int getSelection();
    }

    interface IController {
        void onNavigationSelected(@Navigation int selection);
    }
}