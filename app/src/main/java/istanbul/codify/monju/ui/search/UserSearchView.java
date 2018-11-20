package istanbul.codify.monju.ui.search;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface UserSearchView extends MvpView {

    void onCloseClicked();

    void onUserSearched(String query);

    void onUserClicked(User user);

    void onLoaded(List<User> users);

    void onError(ApiError error);
}
