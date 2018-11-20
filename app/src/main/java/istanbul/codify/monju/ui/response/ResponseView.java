package istanbul.codify.monju.ui.response;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

interface ResponseView extends MvpView {

    void onWordSelectClicked();

    void onLoaded(User user, String text);

    void onLoaded(User user, String text, String wordText, String wordImage);

    void onSendClicked();

    void onSent();

    void onError(ApiError error);

    void onError(Exception exception);

    void onCloseClicked();

    void onProfilePhotoClicked(User user);
}
