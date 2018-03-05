package com.codify.howdy.ui.postdetail;

import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetCommentsRequest;
import com.codify.howdy.api.pojo.request.GetSinglePostRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetCommentsResponse;
import com.codify.howdy.api.pojo.response.GetSinglePostResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.zipper.PostDetail;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

final class PostDetailPresenter extends BasePresenter<PostDetailView> {

    @Override
    public void attachView(PostDetailView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.post_detail_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.post_detail_comment))
                        .flatMap((Function<CharSequence, ObservableSource<String>>) content -> Observable.just(StringUtils.isEmpty(content) ? "" : content.toString().trim()))
                        .flatMap((Function<String, ObservableSource<Boolean>>) message -> Observable.just(!StringUtils.isEmpty(message)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> findViewById(R.id.post_detail_send).setEnabled(enabled)));
    }

    void send(String comment) {

    }

    public void getPost(long postId) {
        GetSinglePostRequest request = new GetSinglePostRequest(postId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getSinglePost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetSinglePostResponse>() {
                            @Override
                            protected void success(GetSinglePostResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    public void getPostDetail(long postId) {
        mDisposables.add(
                Single
                        .zip(
                                observer -> {
                                    GetSinglePostRequest request = new GetSinglePostRequest(postId);
                                    request.token = AccountUtils.token(getContext());

                                    ApiManager
                                            .getInstance()
                                            .getSinglePost(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<GetSinglePostResponse>() {
                                                @Override
                                                protected void success(GetSinglePostResponse response) {
                                                    observer.onSuccess(response.data);
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            });
                                },
                                observer -> {
                                    GetCommentsRequest request = new GetCommentsRequest(postId);
                                    request.token = AccountUtils.token(getContext());

                                    ApiManager
                                            .getInstance()
                                            .getComments(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<GetCommentsResponse>() {
                                                @Override
                                                protected void success(GetCommentsResponse response) {
                                                    observer.onSuccess(response.data);
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            });
                                },
                                PostDetail::new)
                        .subscribe((detail, throwable) -> mView.onLoaded(detail)));
    }
}
