package istanbul.codify.muudy.ui.around;

import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

interface AroundView extends MvpView {

    void onPostLoaded(List<AroundUsers> around, Post post);

    void onPostLoadedError(List<AroundUsers> around);

    void onMoreClicked(ArrayList<User> users);

    void onUserClicked(User user);

    void onCloseClicked();
}
