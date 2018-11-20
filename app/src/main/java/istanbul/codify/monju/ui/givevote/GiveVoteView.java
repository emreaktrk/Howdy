package istanbul.codify.monju.ui.givevote;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GivePointResponse;
import istanbul.codify.monju.ui.base.MvpView;

interface  GiveVoteView extends  MvpView {

    void onGivePointClicked(Float givenPoint);

    void onError(ApiError error);

    void onPointGiven(GivePointResponse response);

    void onPointChanged(Float point);
}
