package istanbul.codify.monju.ui.facebookfriends;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface FacebookFriendsView extends MvpView {

    void onLoaded(List<User> users);

    void onFollowClicked(User user);

    void onUnfollowClicked(User user);

    void onError(ApiError error);

    void onCloseClicked();
}
