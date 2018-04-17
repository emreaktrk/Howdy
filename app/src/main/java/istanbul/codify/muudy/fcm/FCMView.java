package istanbul.codify.muudy.fcm;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface FCMView extends MvpView {

    void onTokenUpdated(String token);

    void onError(ApiError error);
}
