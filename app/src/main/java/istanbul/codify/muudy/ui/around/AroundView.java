package istanbul.codify.muudy.ui.around;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.ui.base.MvpView;

interface AroundView extends MvpView {

    void onError(ApiError error);
}
