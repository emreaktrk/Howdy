package istanbul.codify.monju.ui.mention;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Mention;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;

interface MentionView extends MvpView {

    void onLoaded(ArrayList<User> users);

    void onError(ApiError error);

    void onUserSearched(String query);

    void onMentionClicked(Mention mention);

    void onDoneClicked(ArrayList<User> users);

    void onCloseClicked();

}
