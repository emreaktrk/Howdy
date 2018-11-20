package istanbul.codify.monju.ui.followrequests;

import istanbul.codify.monju.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.FollowRequest;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

public interface FollowRequestsView extends MvpView {

    void onError(ApiError error);

    void onBackClicked();

    void onLoaded(List<FollowRequest> requests);

    void onFollowRequestClicked(FollowRequest request);

    void onAcceptFollowRequestClicked(FollowRequest request);

    void onDeclineFollowRequestClicked(FollowRequest request);

    void onRequestAcceptOrDecline(AnswerFollowResponse answerFollowResponse);
}
