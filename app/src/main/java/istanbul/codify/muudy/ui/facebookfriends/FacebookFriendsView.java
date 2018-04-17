package istanbul.codify.muudy.ui.facebookfriends;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface FacebookFriendsView extends MvpView {

    void onLoaded(List<User> users);

    void onFollowClicked(User user);

    void onUnfollowClicked(User user);

    void onError(ApiError error);

    void onCloseClicked();
}
