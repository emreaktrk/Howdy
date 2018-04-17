package istanbul.codify.muudy.ui.main;

import istanbul.codify.muudy.ui.base.MvpView;

public interface MainView extends MvpView {

    void onHomeClicked();

    void onStatisticClicked();

    void onComposeClicked();

    void onNotificationClicked();

    void onProfileClicked();
}
