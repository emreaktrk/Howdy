package istanbul.codify.muudy.ui.changepassword;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ApiResult;
import istanbul.codify.muudy.ui.base.MvpView;

interface ChangePasswordView extends MvpView {

    void onChangeClicked();

    void onLoaded(ApiResult result);

    void onError(ApiError error);

    void onError(Throwable throwable);

    void onBackClicked();
}
