package istanbul.codify.muudy.ui.feedback;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface FeedbackView extends MvpView {

    void onSendClicked();

    void onLoaded();

    void onError(ApiError error);

    void onError(Exception exception);

    void onBackClicked();
}
