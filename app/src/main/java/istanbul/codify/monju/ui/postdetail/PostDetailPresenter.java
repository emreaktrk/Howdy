package istanbul.codify.monju.ui.postdetail;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.*;
import istanbul.codify.monju.api.pojo.response.*;
import istanbul.codify.monju.helper.OnSwipeDialogCallback;
import istanbul.codify.monju.helper.RecyclerViewHelper;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.zipper.PostDetail;
import istanbul.codify.monju.ui.base.BasePresenter;
import istanbul.codify.monju.ui.home.PostAdapter;

final class PostDetailPresenter extends BasePresenter<PostDetailView> {

    RecyclerViewHelper recyclerViewHelper;

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
        PostDetailAdapter postDetailAdapter = new PostDetailAdapter(detail.post,detail.comments,detail.post.rozetler);
        mDisposables.add(
                postDetailAdapter
                        .likeClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Like clicked");
                            mView.onLikeClicked(cell);
                        }));
        mDisposables.add(
                postDetailAdapter
                        .imageClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Image clicked");
                            mView.onImageClicked(cell);
                        }));
        mDisposables.add(
                postDetailAdapter
                        .videoClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Video clicked");
                            mView.onVideoClicked(cell);
                        }));
        mDisposables.add(
                postDetailAdapter
                        .avatarClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");
                            mView.onAvatarClicked(cell);
                        }));

        mDisposables.add(
                postDetailAdapter
                        .likeCountClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("likeCount clicked");

                            mView.onLikeCountClicked(cell);
                        }));
        mDisposables.add(
                postDetailAdapter
                        .commentAvatarClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");
                            mView.onCommentImageclicked(cell);
                        }));

        findViewById(R.id.post_detail_post, RecyclerView.class).setAdapter(postDetailAdapter);

    /*    CommentAdapter comment = new CommentAdapter(detail.comments);
        mDisposables.add(
                comment
                        .imageClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");
                            mView.onCommentImageclicked(cell);
                        }));*/
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.post_detail_post, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.post_detail_post, RecyclerView.class).setAdapter(postDetailAdapter);

     /*   BadgeAdapter badge = new BadgeAdapter(detail.post.rozetler);
        findViewById(R.id.post_detail_badge, RecyclerView.class).setAdapter(badge);
*/
        findViewById(R.id.post_detail_username, AppCompatTextView.class).setText(detail.post.username);

        if (recyclerViewHelper != null) {
            recyclerViewHelper.detachFromRecyclerView(findViewById(R.id.post_detail_post, RecyclerView.class));
        }

        recyclerViewHelper = new RecyclerViewHelper();
        recyclerViewHelper.setItemViewSwipeEnable(true);

        recyclerViewHelper.initSwipeForPostDetail(findViewById(R.id.post_detail_post, RecyclerView.class),getContext(), detail, new OnSwipeDialogCallback() {
            @Override
            public void onDialogButtonClick(Boolean isYes, int position, Boolean isDelete) {
                postDetailAdapter.notifyDataSetChanged();
                if(isYes) {
                    if (isDelete) {

                        mView.onDeleteComment(detail.comments.get(position).idpostcomment);
                    }
                }
            }


        });
    }

    void deleteComment(long id){
        DeleteCommentRequest request = new DeleteCommentRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.commentid = id;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .deleteComment(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<DeleteCommentResponse>() {
                            @Override
                            protected void success(DeleteCommentResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));

    }

    void collapse() {
        //findViewById(R.id.post_detail_post, RecyclerView.class).setVisibility(View.GONE);
       // findViewById(R.id.post_detail_badge, RecyclerView.class).setVisibility(View.GONE);
    }

    void expand() {
        //findViewById(R.id.post_detail_post, RecyclerView.class).setVisibility(View.VISIBLE);
        //findViewById(R.id.post_detail_badge, RecyclerView.class).setVisibility(View.VISIBLE);
    }
}
