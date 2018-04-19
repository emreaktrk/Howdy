package istanbul.codify.muudy.ui.main;

import istanbul.codify.muudy.ui.base.MvpView;

public interface MainView extends MvpView {

    void onHomeClicked(boolean reselect);

    void onStatisticClicked(boolean reselect);

    void onComposeClicked();

    void onNotificationClicked(boolean reselect);

    void onProfileClicked(boolean reselect);
}
