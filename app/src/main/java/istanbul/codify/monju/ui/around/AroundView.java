package istanbul.codify.monju.ui.around;

import istanbul.codify.monju.model.AroundUsers;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

interface AroundView extends MvpView {

    void onPostLoaded(List<AroundUsers> around, Post post);

    void onPostLoadedError(List<AroundUsers> around);

    void onMoreClicked(ArrayList<User> users);

    void onUserClicked(User user);

    void onCloseClicked();
}
