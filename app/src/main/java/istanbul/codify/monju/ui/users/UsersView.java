package istanbul.codify.monju.ui.users;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Follow;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface UsersView extends MvpView {

    void onUserSearched(String query);

    void onUserClicked(User user);

    void onFollowClicked(Follow follow);

    void onLoaded(List<User> users);

    void onError(ApiError error);

    void onCloseClicked();

}
