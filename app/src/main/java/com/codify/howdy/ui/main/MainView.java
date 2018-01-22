package com.codify.howdy.ui.main;

import com.codify.howdy.ui.base.MvpView;

public interface MainView extends MvpView {

    void onHomeClicked();

    void onStatisticClicked();

    void onComposeClicked();

    void onNotificationClicked();

    void onProfileClicked();
}
