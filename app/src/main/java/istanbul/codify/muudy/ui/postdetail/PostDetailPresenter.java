package istanbul.codify.muudy.ui.postdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.zipper.PostDetail;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.home.PostAdapter;

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


        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.post_detail_send))
                        .flatMap((Function<Object, ObservableSource<String>>) o -> {
                            String comment = findViewById(R.id.post_detail_comment, AppCompatEditText.class).getText().toString().trim();
                            return Observable.just(comment);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(comment -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked(comment);
                        }));
    }

    void send(long postId, String comment) {
        findViewById(R.id.post_detail_comment, AppCompatEditText.class).setText(null);

        CommentPostRequest request = new CommentPostRequest(postId, comment);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .commentPost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CommentPostResponse>() {
                            @Override
                            protected void success(CommentPostResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void getPostDetail(long postId) {
        mDisposables.add(
                Single
                        .zip(
                                observer -> {
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
                                                            observer.onSuccess(response.data);
                                                        }

                                                        @Override
                                                        protected void error(ApiError error) {
                                                            Logcat.e(error);

                                                            mView.onError(error);
                                                        }
                                                    })
                                    );
                                },
                                observer -> {
                                    GetCommentsRequest request = new GetCommentsRequest(postId);
                                    request.token = AccountUtils.tokenLegacy(getContext());

                                    mDisposables.add(
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
                                                    })
                                    );
                                },
                                PostDetail::new)
                        .subscribe((detail, throwable) -> mView.onLoaded(detail)));
    }

    void like(long postId) {
        LikePostRequest request = new LikePostRequest(postId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .likePost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<LikePostResponse>() {
                            @Override
                            protected void success(LikePostResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void dislike(long postId) {
        DislikePostRequest request = new DislikePostRequest(postId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .dislikePost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<DislikePostResponse>() {
                            @Override
                            protected void success(DislikePostResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(@NonNull PostDetail detail) {
        PostAdapter post = new PostAdapter(detail.post);
        mDisposables.add(
                post
                        .likeClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Like clicked");
                            mView.onLikeClicked(cell);
                        }));
        mDisposables.add(
                post
                        .imageClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Image clicked");
                            mView.onImageClicked(cell);
                        }));
        mDisposables.add(
                post
                        .videoClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Video clicked");
                            mView.onVideoClicked(cell);
                        }));
        mDisposables.add(
                post
                        .avatarClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");
                            mView.onAvatarClicked(cell);
                        }));
        findViewById(R.id.post_detail_post, RecyclerView.class).setAdapter(post);
    }
}
