package istanbul.codify.muudy.ui.response;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface ResponseView extends MvpView {

    void onWordSelectClicked();

    void onLoaded(User user, String text);

    void onSendClicked();

    void onSent();

    void onError(ApiError error);

    void onError(Exception exception);

    void onCloseClicked();

    void onProfilePhotoClicked(User user);
}
