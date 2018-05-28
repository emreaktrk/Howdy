package istanbul.codify.muudy.ui.profile;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.home.PostAdapter;
import istanbul.codify.muudy.view.NumberView;

import java.util.ArrayList;
import java.util.List;

final class ProfilePresenter extends BasePresenter<ProfileView> {

    private int selectedIndex = 0;

    @Override
    public void attachView(ProfileView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_settings))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Settings clicked");

                            view.onSettingsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_number_followed))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Followers clicked");

                            view.onFollowersClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_number_following))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Followings clicked");

                            view.onFollowingsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_posts))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Posts clicked");

                            setSelected(0);
                            view.onPostsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_tops))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Tops clicked");

                            setSelected(1);
                            view.onTopsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_games))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Games clicked");

                            setSelected(2);
                            view.onGamesClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_series))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Series clicked");

                            setSelected(3);
                            view.onSeriesClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_films))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Films clicked");

                            setSelected(4);
                            view.onFilmsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_books))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Books clicked");

                            setSelected(5);
                            view.onBooksClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_picture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture clicked");

                            view.onPictureClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_facebook))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Facebook clicked");

                            view.onFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_twitter))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Twitter clicked");

                            view.onTwitterClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_instagram))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Instagram clicked");

                            view.onInstagramClicked();
                        }));

        mDisposables.add(
                RxSwipeRefreshLayout
                        .refreshes(findViewById(R.id.profile_refresh))
                        .subscribe(o -> {
                            Logcat.v("Refresh clicked");

                            mView.onRefresh();
                        }));

        findViewById(R.id.profile_appbar, AppBarLayout.class)
                .addOnOffsetChangedListener((layout, offset) -> findViewById(R.id.profile_refresh).setEnabled(offset == 0));
    }

    void bind(User user) {
        findViewById(R.id.profile_username, AppCompatTextView.class).setText(user.username);
        findViewById(R.id.profile_fullname, AppCompatTextView.class).setText(user.namesurname);

        ArrayList<String> awards = new ArrayList<>();
        if (!TextUtils.isEmpty(user.award1Text)) {
            awards.add(user.award1Text);
        }

        if (!TextUtils.isEmpty(user.award2Text)) {
            awards.add(user.award2Text);
        }

        if (!TextUtils.isEmpty(user.award3Text)) {
            awards.add(user.award3Text);
        }

        findViewById(R.id.profile_facebook).setVisibility(StringUtils.isEmpty(user.facebooklink) ? View.GONE : View.VISIBLE);
        findViewById(R.id.profile_twitter).setVisibility(StringUtils.isEmpty(user.twitterlink) ? View.GONE : View.VISIBLE);
        findViewById(R.id.profile_instagram).setVisibility(StringUtils.isEmpty(user.instagramlink) ? View.GONE : View.VISIBLE);

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < awards.size(); i++) {
            text.append(awards.get(i));

            if (i != awards.size() - 1) {
                text.append(", ");
            }
        }
        findViewById(R.id.profile_award, AppCompatTextView.class).setText(text);

        NumberView posts = findViewById(R.id.profile_number_posts, NumberView.class);
        posts.setText("Paylaşım");
        posts.setValue(user.postcount);

        NumberView followed = findViewById(R.id.profile_number_followed, NumberView.class);
        followed.setText("Takip Eden");
        followed.setValue(user.followedcount);

        NumberView following = findViewById(R.id.profile_number_following, NumberView.class);
        following.setText("Takip Edilen");
        following.setValue(user.followercount);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.profile_picture, CircleImageView.class));
    }

    void refresh() {
        if (selectedIndex == 0) {
            posts();
        } else {
            Long categoryId = Category.GAME;
            if (selectedIndex == 2) {
                categoryId = Category.GAME;
            } else if (selectedIndex == 3) {
                categoryId = Category.GAME;
            } else if (selectedIndex == 4) {
                categoryId = Category.GAME;
            } else if (selectedIndex == 5) {
                categoryId = Category.GAME;
            }
            stars(categoryId);
        }
    }

    void posts() {
        User me = AccountUtils.me(getContext());

        GetUserProfileRequest request = new GetUserProfileRequest(me.iduser);
        request.token = AccountUtils.tokenLegacy(getContext());

        //   findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(true);
        selectedIndex = 0;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserProfile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                            @Override
                            protected void success(GetUserProfileResponse response) {
                                //mView.onLoadedPosts(response.data.postlist);
                                mView.onLoaded(response.data.postlist, selectedIndex);
                                findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                mView.onError(error);
                            }
                        }));
    }

    void bindPosts(List<Post> posts) {
        setSelected(0);

        PostAdapter post = new PostAdapter(posts);
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
                        .deleteClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Delete clicked");

                            mView.onDeleteClicked(cell);
                        }));
        findViewById(R.id.profile_recycler, RecyclerView.class).setAdapter(post);

        if (posts.size() == 0) {
            findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.GONE);
        } else {
            findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.GONE);
            findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        }


    }

    void stars(long categoryId) {
        if (categoryId == Category.GAME) {
            selectedIndex = 2;
        } else if (categoryId == Category.SERIES) {
            selectedIndex = 3;
        } else if (categoryId == Category.FILM) {
            selectedIndex = 4;
        } else if (categoryId == Category.BOOK) {
            selectedIndex = 5;
        }

        findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(true);
        User me = AccountUtils.me(getContext());

        GetUserPostsRequest request = new GetUserPostsRequest(me.iduser);
        request.token = AccountUtils.tokenLegacy(getContext());
        request.categoryId = categoryId;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserPosts(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserPostsResponse>() {
                            @Override
                            protected void success(GetUserPostsResponse response) {
                                //   mView.onLoadedStars(response.data);
                                mView.onLoaded(response.data, selectedIndex);
                                findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                                findViewById(R.id.profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                            }
                        }));
    }

    void bindStars(List<Post> stars) {
        StarAdapter post = new StarAdapter(stars);
        findViewById(R.id.profile_recycler, RecyclerView.class).setAdapter(post);

        if (stars.size() == 0) {
            findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.GONE);
        } else {
            findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.GONE);
            findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        }
    }

    int getSelected() {
        LinearLayoutCompat tabs = findViewById(R.id.profile_tabs, LinearLayoutCompat.class);
        for (int i = 0; i < tabs.getChildCount(); i++) {
            View child = tabs.getChildAt(i);
            if (child.isSelected()) {
                return i;
            }
        }

        return -1;
    }

    private void setSelected(int position) {
        LinearLayoutCompat tabs = findViewById(R.id.profile_tabs, LinearLayoutCompat.class);
        for (int i = 0; i < tabs.getChildCount(); i++) {
            View child = tabs.getChildAt(i);
            child.setSelected(i == position);
        }
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

    void tops() {
        User me = AccountUtils.me(getContext());

        GetUserWeeklyTopRequest request = new GetUserWeeklyTopRequest(me.iduser);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserWeeklyTop(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserWeeklyTopResponse>() {
                            @Override
                            protected void success(GetUserWeeklyTopResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
