package istanbul.codify.muudy.ui.users;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Follow;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface UsersView extends MvpView {

    void onUserSearched(String query);

    void onUserClicked(User user);

    void onFollowClicked(Follow follow);

    void onLoaded(List<User> users);

    void onError(ApiError error);

    void onCloseClicked();

}
