package istanbul.codify.muudy.ui.around;

import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface AroundView extends MvpView {

    void onMoreClicked(List<User> users);

    void onUserClicked(User user);

    void onCloseClicked();
}
