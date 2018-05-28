package istanbul.codify.muudy.ui.givevote;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GivePointResponse;
import istanbul.codify.muudy.ui.base.MvpView;

interface  GiveVoteView extends  MvpView {

    void onGivePointClicked(Float givenPoint);

    void onError(ApiError error);

    void onPointGiven(GivePointResponse response);

    void onPointChanged(Float point);
}
