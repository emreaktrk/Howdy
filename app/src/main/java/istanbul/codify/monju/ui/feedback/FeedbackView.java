package istanbul.codify.monju.ui.feedback;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.ui.base.MvpView;

interface FeedbackView extends MvpView {

    void onSendClicked();

    void onLoaded();

    void onError(ApiError error);

    void onError(Exception exception);

    void onBackClicked();
}
