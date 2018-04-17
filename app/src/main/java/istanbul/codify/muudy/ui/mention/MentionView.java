package istanbul.codify.muudy.ui.mention;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Mention;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;

interface MentionView extends MvpView {

    void onLoaded(ArrayList<User> users);

    void onError(ApiError error);

    void onUserSearched(String query);

    void onMentionClicked(Mention mention);

    void onDoneClicked(ArrayList<User> users);

    void onCloseClicked();

}
