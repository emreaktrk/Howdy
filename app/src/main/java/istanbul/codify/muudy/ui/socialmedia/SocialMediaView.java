package istanbul.codify.muudy.ui.socialmedia;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface SocialMediaView extends MvpView {

    void onSaveClicked();

    void onLoaded();

    void onError(ApiError error);

    void onBackClicked();
}
