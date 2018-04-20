package istanbul.codify.muudy.ui.createuser;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface CreateUserView extends MvpView {

    void onCreateClicked();

    void onLoaded(User user);

    void onError(ApiError error);

    void onError(Exception exception);

}
