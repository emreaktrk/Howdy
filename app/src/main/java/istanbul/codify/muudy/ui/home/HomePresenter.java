package istanbul.codify.muudy.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

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
import istanbul.codify.muudy.helper.BlurBuilder;
import istanbul.codify.muudy.helper.RecyclerViewHelper;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.More;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.Wall;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.utils.SharedPrefs;

import java.util.ArrayList;

final class HomePresenter extends BasePresenter<HomeView> {

    private RecyclerViewHelper mHelper;

    Wall mWall;
    EmotionAdapter emotion;
    PostAdapter post;

    @Override
    public void attachView(HomeView view, View root) {
        super.attachView(view, root);

        findViewById(R.id.home_emotion_recycler, RecyclerView.class).setVisibility(View.GONE);
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
    void getWall(Context context, @Nullable More more, @Nullable ArrayList<AroundUsers> aroundUsers, @Nullable Boolean isRefreshing) {
        if(isRefreshing == null){
            isRefreshing = false;
        }
        Boolean finalIsRefreshing = isRefreshing;
        LocationServices
                .getFusedLocationProviderClient(context)
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null) {

                        Location location = new Location("default");
                        location.setLatitude(SharedPrefs.getLatitude(getContext()));
                        location.setLongitude(SharedPrefs.getLongitude(getContext()));
                        return location;
                    }else{
                        SharedPrefs.setLatitude(result.getLatitude()+"",getContext());
                        SharedPrefs.setLongitude(result.getLongitude()+"",getContext());
                    }

                    return result;
                })
                .addOnSuccessListener(location -> {
                    findViewById(R.id.home_refresh, SwipeRefreshLayout.class).setRefreshing(finalIsRefreshing);
                    mDisposables.add(
                            Single
                                    .just(location)
                                    .subscribeOn(Schedulers.io())
                                    .flatMap((Function<Location, SingleSource<GetWallResponse>>) point -> {
                                        GetWallRequest request = new GetWallRequest(point);
                                        request.token = AccountUtils.token(getContext());
                                        if (more != null) {
                                            request.page = more.page + 1;
                                        }

                                        return ApiManager
                                                .getInstance()
                                                .getWall(request)
                                                .observeOn(AndroidSchedulers.mainThread());
                                    })
                                    .subscribe(new ServiceConsumer<GetWallResponse>() {
                                        @Override
                                        protected void success(GetWallResponse response) {
                                            mWall = response.data;
                                            if (more == null) {

                                                if (aroundUsers != null && !aroundUsers.isEmpty()){
                                                    mView.onLoaded(response.data,aroundUsers,null);
                                                }else {
                                                    mView.onLoaded(response.data);
                                                }
                                            } else {
                                                mView.onMoreLoaded(response.data.posts, more);
                                            }

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

    void showNewMessageDot(boolean show) {
        findViewById(R.id.home_has_message_dot).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    boolean hasNewMessageDot() {
        return findViewById(R.id.home_has_message_dot).getVisibility() == View.VISIBLE;
    }

    void takeBlurredImage(ArrayList<AroundUsers> arounds, Long postId){
        View content = findViewById(R.id.home_refresh).getRootView();

        Bitmap bitmap = BlurBuilder.blur(content);
        mView.onBlurredImageTaken(arounds, bitmap, postId);
    }

    void bind(Wall wall, @Nullable ArrayList<AroundUsers> aroundUsers, @Nullable Long postId) {


            emotion = new EmotionAdapter(wall.nearEmotions);
            findViewById(R.id.home_emotion_recycler, RecyclerView.class).setAdapter(emotion);

        mDisposables.add(
                emotion
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Emotion clicked");

                            mView.onEmotionClicked(cell);
                        }));

        findViewById(R.id.home_emotion_recycler, RecyclerView.class).setVisibility((wall.nearEmotions == null || wall.nearEmotions.isEmpty()) ? View.GONE : View.VISIBLE);

        findViewById(R.id.home_emotion_recycler, RecyclerView.class).setItemAnimator(null);

            post = new PostAdapter(wall.posts, wall.recomendedUsers);
            findViewById(R.id.home_post_recycler, RecyclerView.class).setAdapter(post);

     /*

        RecyclerView.ItemAnimator animator = findViewById(R.id.home_post_recycler, RecyclerView.class).getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }*/

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
                        .likeCountClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("likeCount clicked");

                            mView.onLikeCountClicked(cell);
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
/*        mDisposables.add(
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
        */
        mDisposables.add(
                post
                        .morePages()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(more -> {
                            Logcat.v("More pages requested");

                            mView.onMorePage(more);
                        }));



        if (wall.posts.size() > 0) {
            findViewById(R.id.home_post_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
            findViewById(R.id.home_post_no_post, AppCompatTextView.class).setVisibility(View.GONE);
        } else {
            findViewById(R.id.home_post_recycler, RecyclerView.class).setVisibility(View.GONE);
            findViewById(R.id.home_post_no_post, AppCompatTextView.class).setVisibility(View.VISIBLE);
        }

        if (mHelper != null) {
            mHelper.detachFromRecyclerView(findViewById(R.id.home_post_recycler, RecyclerView.class));
        }

        mHelper = new RecyclerViewHelper();
        mHelper.setItemViewSwipeEnable(true);

        mHelper.initSwipe(findViewById(R.id.home_post_recycler, RecyclerView.class), getContext(), wall, findViewById(R.id.home_refresh, SwipeRefreshLayout.class), (isYes, position, isDelete) -> {

            if (isYes) {
                if (isDelete) {
                    mView.onDeleteClicked(wall.posts.get(position));
                    Logcat.d("Deleting post: " + wall.posts.get(position).post_text + "");
                } else {
                    Logcat.d("Muudy post: " + wall.posts.get(position).post_text + "");
                    mView.onMuudyClicked(wall.posts.get(position));
                }

                post.notifyDataSetChanged();
            } else {
                post.notifyDataSetChanged();
            }
        });

        final boolean[] isLoaded = {false};
        findViewById(R.id.home_post_recycler, RecyclerView.class).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Logcat.d("recycler yüklendi");
                if (!isLoaded[0] && aroundUsers != null && !aroundUsers.isEmpty()) {

                    Logcat.d("recycler yüklendi if içi");
                    isLoaded[0] = true;
                    mView.onPostsLoaded(aroundUsers, postId);
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
                                Toast.makeText(getContext(), "Muudy başarıyla gönderildi", Toast.LENGTH_SHORT).show();
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

    void add(ArrayList<Post> posts, More more) {
        RecyclerView.Adapter adapter = findViewById(R.id.home_post_recycler, RecyclerView.class).getAdapter();
        if (adapter instanceof PostAdapter) {
            PostAdapter post = (PostAdapter) adapter;
            post.add(posts, more);
            post.notifyDataSetChanged();
        }
    }

    void scrollToTop() {
        findViewById(R.id.home_post_recycler, RecyclerView.class).scrollToPosition(0);
    }
}
