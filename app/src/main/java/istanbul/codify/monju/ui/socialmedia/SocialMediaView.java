package istanbul.codify.monju.ui.socialmedia;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.ui.base.MvpView;

interface SocialMediaView extends MvpView {

    void onSaveClicked();

    void onLoaded();

    void onError(ApiError error);

    void onBackClicked();
}
