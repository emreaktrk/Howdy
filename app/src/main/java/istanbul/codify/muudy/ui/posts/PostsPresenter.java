package istanbul.codify.muudy.ui.posts;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.home.PostAdapter;

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

    void getPosts(Activity activity) {
        findViewById(R.id.posts_title, AppCompatTextView.class).setText(activity.activities_title);
        GetActivityPostsRequest request = new GetActivityPostsRequest(activity.idactivities);

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
                        .followClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Follow clicked");

                            mView.onFollowClicked(cell);
                        }));
        mDisposables.add(
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
                        }));

        findViewById(R.id.posts_recycler, RecyclerView.class).setAdapter(adapter);
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

    void delete(Post post) {
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
                                mView.onPostDeleted();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
