package istanbul.codify.muudy.ui.search;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface UserSearchView extends MvpView {

    void onCloseClicked();

    void onUserSearched(String query);

    void onUserClicked(User user);

    void onLoaded(List<User> users);

    void onError(ApiError error);
}
