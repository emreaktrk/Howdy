package istanbul.codify.muudy.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.DislikePostRequest;
import istanbul.codify.muudy.api.pojo.request.GetWallRequest;
import istanbul.codify.muudy.api.pojo.request.LikePostRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.DislikePostResponse;
import istanbul.codify.muudy.api.pojo.response.GetWallResponse;
import istanbul.codify.muudy.api.pojo.response.LikePostResponse;
import istanbul.codify.muudy.logcat.Logcat;
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
    }

    @SuppressLint({"MissingPermission"})
    public void getWall(Context context) {
        LocationServices
                .getFusedLocationProviderClient(context)
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null) {
                        Location location = new Location("default");
                        location.setLatitude(40.991955);
                        location.setLatitude(28.712913);
                        return location;
                    }

                    return result;
                })
                .addOnSuccessListener(location ->
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
                                            }

                                            @Override
                                            protected void error(ApiError error) {
                                                Logcat.e(error);

                                                mView.onError(error);
                                            }
                                        })))
                .addOnFailureListener(Logcat::e);
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
        findViewById(R.id.home_post_recycler, RecyclerView.class).setAdapter(post);
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
}