package istanbul.codify.muudy.ui.main;

import java.util.ArrayList;

import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.ui.base.MvpView;

public interface MainView extends MvpView {

    void onHomeClicked(boolean reselect);

    void openHome(boolean reselect, ArrayList<AroundUsers> around, Long postId);

    void onStatisticClicked(boolean reselect);

    void onComposeClicked();

    void onNotificationClicked(boolean reselect);

    void onProfileClicked(boolean reselect);
}
