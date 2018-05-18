package istanbul.codify.muudy.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.Wall;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class HomePresenter extends BasePresenter<HomeView> {

    @Override
    public void attachView(HomeView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.home_search))
                        .subscribe(o -> {
                            Logcat.v("Search clicked");

                            view.onSearchClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.home_messages))
                        .subscribe(o -> {
                            Logcat.v("Chat clicked");

                            view.onMessagesClicked();
                        }));

        mDisposables.add(
                RxSwipeRefreshLayout
                        .refreshes(findViewById(R.id.home_refresh))
                        .subscribe(o -> {
                            Logcat.v("Refresh clicked");

                            view.onRefresh();
                        }));
    }

    @SuppressLint({"MissingPermission"})
    void getWall(Context context) {
        LocationServices
                .getFusedLocationProviderClient(context)
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null || BuildConfig.DEBUG) {
                        Location location = new Location("default");
                        location.setLatitude(40.991955);
                        location.setLongitude(28.712913);
                        return location;
                    }

                    return result;
                })
                .addOnSuccessListener(location -> {
                    findViewById(R.id.home_refresh, SwipeRefreshLayout.class).setRefreshing(true);

                    mDisposables.add(
                            Single
                                    .just(location)
                                    .subscribeOn(Schedulers.io())
                                    .flatMap((Function<Location, SingleSource<GetWallResponse>>) point -> {
                                        GetWallRequest request = new GetWallRequest(point);
                                        request.token = AccountUtils.token(getContext());

                                        return ApiManager
                                                .getInstance()
                                                .getWall(request)
                                                .observeOn(AndroidSchedulers.mainThread());
                                    })
                                    .subscribe(new ServiceConsumer<GetWallResponse>() {
                                        @Override
                                        protected void success(GetWallResponse response) {
                                            mView.onLoaded(response.data);

                                            findViewById(R.id.home_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                        }

                                        @Override
                                        protected void error(ApiError error) {
                                            Logcat.e(error);

                                            mView.onError(error);

                                            findViewById(R.id.home_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                        }
                                    }));
                })
                .addOnFailureListener(Logcat::e);
    }

    void bind(Boolean hasMessage){
        if (hasMessage){
            findViewById(R.id.home_has_message_dot, AppCompatImageView.class).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.home_has_message_dot, AppCompatImageView.class).setVisibility(View.GONE);
        }

    }
    void bind(Wall wall) {
        EmotionAdapter emotion = new EmotionAdapter(wall.nearEmotions);
        mDisposables.add(
                emotion
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Emotion clicked");

                            mView.onEmotionClicked(cell);
                        }));
        findViewById(R.id.home_emotion_recycler, RecyclerView.class).setAdapter(emotion);
        findViewById(R.id.home_emotion_recycler, RecyclerView.class).setVisibility((wall.nearEmotions == null || wall.nearEmotions.isEmpty()) ? View.GONE : View.VISIBLE);

        PostAdapter post = new PostAdapter(wall.posts, wall.recomendedUsers);
        mDisposables.add(
                post
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Post clicked");

                            mView.onPostClicked(cell);
                        }));
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
        mDisposables.add(
                post
                        .userClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Avatar clicked");

                            mView.onUserClicked(cell);
                        }));
        mDisposables.add(
                post
                        .followClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Follow clicked");

                            mView.onFollowClicked(cell);
                        }));
        mDisposables.add(
                post
                        .deleteClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Delete clicked");

                            mView.onDeleteClicked(cell);
                        }));
        mDisposables.add(
                post
                        .muudyClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Muudy clicked");

                            mView.onMuudyClicked(cell);
                        }));


/// TODO: 16.05.2018 Paging eklenecek
       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        findViewById(R.id.home_post_recycler, RecyclerView.class).setLayoutManager(linearLayoutManager);*/
        findViewById(R.id.home_post_recycler, RecyclerView.class).setAdapter(post);
  /*      findViewById(R.id.home_post_recycler, RecyclerView.class).setOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    getWall(getContext());
            }
        });*/

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
