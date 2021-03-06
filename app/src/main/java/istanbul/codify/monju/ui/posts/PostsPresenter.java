package istanbul.codify.monju.ui.posts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.*;
import istanbul.codify.monju.api.pojo.response.*;
import istanbul.codify.monju.helper.OnSwipeDialogCallback;
import istanbul.codify.monju.helper.RecyclerViewHelper;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Activity;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.ui.base.BasePresenter;
import istanbul.codify.monju.ui.home.PostAdapter;

import java.util.List;

final class PostsPresenter extends BasePresenter<PostsView> {

    @Override
    public void attachView(PostsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.posts_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));
    }
    String mWord;
    Activity mActivity;

    void getPosts(@NonNull Activity activity, @Nullable String word) {
        mWord = word;
        mActivity = activity;
        findViewById(R.id.posts_title, AppCompatTextView.class).setText(activity.activities_title);
        GetActivityPostsRequest request = new GetActivityPostsRequest(activity.idactivities);
        request.token = AccountUtils.tokenLegacy(getContext());
        if (word != null) {
            request.word = word;
        }

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getActivityPosts(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetActivityPostsResponse>() {
                            @Override
                            protected void success(GetActivityPostsResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<Post> posts) {
        PostAdapter adapter = new PostAdapter(posts, null);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Post clicked");

                            mView.onPostClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .likeClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Like clicked");

                            mView.onLikeClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .imageClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Image clicked");

                            mView.onImageClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .videoClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Video clicked");

                            mView.onVideoClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .avatarClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");

                            mView.onAvatarClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .userClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");

                            mView.onUserClicked(cell);
                        }));

        mDisposables.add(
                adapter
                        .likeCountClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("likeCount clicked");

                            mView.onLikeCountClicked(cell);
                        }));

      /*  mDisposables.add(
                adapter
                        .deleteClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Delete clicked");

                            mView.onDeleteClicked(cell);
                        }));
        mDisposables.add(
                adapter
                        .muudyClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Muudy clicked");

                            mView.onMuudyClicked(cell);
                        }));*/

        findViewById(R.id.posts_recycler, RecyclerView.class).setAdapter(adapter);

        RecyclerViewHelper recyclerViewHelper = new RecyclerViewHelper();
        recyclerViewHelper.setItemViewSwipeEnable(true);

        recyclerViewHelper.initSwipe(findViewById(R.id.posts_recycler, RecyclerView.class),getContext(), posts, new OnSwipeDialogCallback() {
            @Override
            public void onDialogButtonClick(Boolean isYes, int position, Boolean isDelete) {
                adapter.notifyDataSetChanged();
                if(isYes) {
                    if (isDelete) {
                        mView.onDeleteClicked(posts.get(position), adapter);
                        Logcat.d("silinecek post: " + posts.get(position).post_text + "");
                    } else {
                        Logcat.d("muudy post: " + posts.get(position).post_text + "");
                        mView.onMuudyClicked(posts.get(position));

                    }
                }
            }


        });
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

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void sayHi(long userId) {
        SayHiRequest request = new SayHiRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = userId;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sayHi(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SayHiResponse>() {
                            @Override
                            protected void success(SayHiResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void delete(Post post,PostAdapter adapter) {
        DeletePostRequest request = new DeletePostRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.postid = post.idpost;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .deletePost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<DeletePostResponse>() {
                            @Override
                            protected void success(DeletePostResponse response) {
                                getPosts(mActivity,mWord);

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void setTitle(String title) {
        findViewById(R.id.posts_title, AppCompatTextView.class).setText(title);
    }
}
