package istanbul.codify.muudy.ui.weeklytopuser;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.WeeklyTopUser;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;

public interface WeeklyTopUsersView extends MvpView {

    void onError(ApiError error);

    void onLoaded(ArrayList<WeeklyTopUser> weeklyTopUsers);

    void onUserPhotoClicked(User user);

    void onCloseClick();
}
