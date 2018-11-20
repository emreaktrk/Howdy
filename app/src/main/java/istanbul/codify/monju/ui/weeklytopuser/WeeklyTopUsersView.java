package istanbul.codify.monju.ui.weeklytopuser;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.WeeklyTopUser;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;

public interface WeeklyTopUsersView extends MvpView {

    void onError(ApiError error);

    void onLoaded(ArrayList<WeeklyTopUser> weeklyTopUsers);

    void onUserPhotoClicked(User user);

    void onCloseClick();
}
