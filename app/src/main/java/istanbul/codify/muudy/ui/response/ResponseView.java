package istanbul.codify.muudy.ui.response;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface ResponseView extends MvpView {

    void onWordSelectClicked();

    void onSendClicked();

    void onSent();

    void onError(ApiError error);

    void onError(Exception exception);

    void onCloseClicked();
}
