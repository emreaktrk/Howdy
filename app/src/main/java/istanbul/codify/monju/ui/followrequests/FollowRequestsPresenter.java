package istanbul.codify.monju.ui.followrequests;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.AnswerFollowRequest;
import istanbul.codify.monju.api.pojo.request.GetNotificationsMeRequest;
import istanbul.codify.monju.api.pojo.response.AnswerFollowResponse;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetNotificationsMeResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.FollowRequest;
import istanbul.codify.monju.model.FollowResponse;
import istanbul.codify.monju.ui.base.BasePresenter;

import java.util.List;

public class FollowRequestsPresenter extends BasePresenter<FollowRequestsView> {
    @Override
    public void attachView(FollowRequestsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.follow_request_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.follow_request_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.follow_request_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void getNotifications() {
        GetNotificationsMeRequest request = new GetNotificationsMeRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getNotificationsMe(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetNotificationsMeResponse>() {
                            @Override
                            protected void success(GetNotificationsMeResponse response) {
                                mView.onLoaded(response.data.followRequests);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<FollowRequest> requests) {
        FollowRequestAdapter adapter = new FollowRequestAdapter(requests);


        mDisposables.add(
                adapter
                        .followRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onFollowRequestClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .acceptFollowRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onAcceptFollowRequestClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .declineFollowRequestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            mView.onDeclineFollowRequestClicked(cell);
                        }));

        findViewById(R.id.follow_request_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void acceptFollowRequest(FollowRequest followRequest){
        AnswerFollowRequest request = new AnswerFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = followRequest.user.iduser;
        request.answer = FollowResponse.ACCEPT;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerFollowResponse>() {
                            @Override
                            protected void success(AnswerFollowResponse response) {
                                if(response.data.r.toString().equals("OK")) {
                                    mView.onRequestAcceptOrDecline(response);
                                }else{
                                    mView.onError(new ApiError(response.errMes));
                                }
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void declineFollowRequest(FollowRequest followRequest){
        AnswerFollowRequest request = new AnswerFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = followRequest.user.iduser;
        request.answer = FollowResponse.DELETE;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerFollowResponse>() {
                            @Override
                            protected void success(AnswerFollowResponse response) {
                                if(response.data.r.toString().equals("OK")) {
                                    mView.onRequestAcceptOrDecline(response);
                                }else{
                                    mView.onError(new ApiError(response.errMes));
                                }
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
