package istanbul.codify.monju.ui.changepassword;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.ApiResult;
import istanbul.codify.monju.ui.base.MvpView;

interface ChangePasswordView extends MvpView {

    void onChangeClicked();

    void onLoaded(ApiResult result);

    void onError(ApiError error);

    void onError(Throwable throwable);

    void onBackClicked();
}
