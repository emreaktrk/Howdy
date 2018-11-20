package istanbul.codify.monju.ui.createuser;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

interface CreateUserView extends MvpView {

    void onCreateClicked();

    void onLoaded(User user);

    void onError(ApiError error);

    void onError(Exception exception);

}
