package istanbul.codify.monju.fcm;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.ui.base.MvpView;

interface FCMView extends MvpView {

    void onTokenUpdated(String token);

    void onError(ApiError error);
}
