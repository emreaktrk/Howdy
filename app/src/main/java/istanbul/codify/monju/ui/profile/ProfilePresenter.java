package istanbul.codify.monju.ui.profile;

import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.*;
import android.text.TextUtils;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.*;
import istanbul.codify.monju.api.pojo.response.*;
import istanbul.codify.monju.helper.RecyclerViewHelper;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Category;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.UserTop;
import istanbul.codify.monju.ui.base.BasePresenter;
import istanbul.codify.monju.ui.home.PostAdapter;
import istanbul.codify.monju.utils.PicassoHelper;
import istanbul.codify.monju.view.NumberView;

import java.util.ArrayList;
import java.util.List;

final class ProfilePresenter extends BasePresenter<ProfileView> {

    User mUser;
    RecyclerViewHelper mRecyclerHelper;
    int selectedIndex = 0;
    List<Post> posts;
    List<Post> stars;
    ArrayList<UserTop> userTops;
    @Override
    public void attachView(ProfileView view, View root) {
        super.attachView(view, root);

        findViewById(R.id.profile_coordinator).setVisibility(View.INVISIBLE);
        findViewById(R.id.profile_recycler, RecyclerView.class).setItemAnimator(null);

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
                RxView
                        .clicks(findViewById(R.id.profile_edit))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Edit clicked");

                            view.onEditClicked();
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
        findViewById(R.id.profile_coordinator).setVisibility(View.VISIBLE);
        mUser = user;
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
        followed.setValue(user.followercount);

        NumberView following = findViewById(R.id.profile_number_following, NumberView.class);
        following.setText("Takip Edilen");
        following.setValue(user.followedcount);

        PicassoHelper.setImageWithPlaceHolder(findViewById(R.id.profile_picture, CircleImageView.class),user.imgpath1,R.drawable.ic_avatar);
        /*
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.profile_picture, CircleImageView.class));
                */
    }

    void refresh() {
        if (selectedIndex == 0) {
            posts();
        } else if (selectedIndex == 1) {
            tops();
        } else {
            Long categoryId = Category.GAME;
            if (selectedIndex == 2) {
                categoryId = Category.GAME;
            } else if (selectedIndex == 3) {
                categoryId = Category.SERIES;
            } else if (selectedIndex == 4) {
                categoryId = Category.FILM;
            } else if (selectedIndex == 5) {
                categoryId = Category.BOOK;
            }
            stars(categoryId);
        }


    }

    void posts() {
        User me = AccountUtils.me(getContext());

        GetUserProfileRequest request = new GetUserProfileRequest(me.iduser);
        request.token = AccountUtils.tokenLegacy(getContext());

        selectedIndex = 0;
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserProfile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                            @Override
                            protected void success(GetUserProfileResponse response) {
                                posts = response.data.postlist;
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

        removeDivider();
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
                        .likeCountClick()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("likeCount clicked");

                            mView.onLikeCountClicked(cell);
                        }));
        findViewById(R.id.profile_recycler, RecyclerView.class).setAdapter(post);

        if (mRecyclerHelper != null) {
            mRecyclerHelper.detachFromRecyclerView(findViewById(R.id.profile_recycler, RecyclerView.class));
        }

        mRecyclerHelper = new RecyclerViewHelper();
        mRecyclerHelper.setItemViewSwipeEnable(true);

        mRecyclerHelper.initSwipe(findViewById(R.id.profile_recycler, RecyclerView.class), getContext(), posts, (isYes, position, isDelete) -> {
            if (isYes) {
                if (isDelete) {
                    mView.onDeleteClicked(posts.get(position));
                    Logcat.d("Deleting post: " + posts.get(position).post_text + "");
                }

                post.notifyDataSetChanged();
            } else {
                post.notifyDataSetChanged();
            }
        });

        if (posts.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
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
                                stars = response.data;
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
        mRecyclerHelper.setItemViewSwipeEnable(false);
        mRecyclerHelper.detachFromRecyclerView(findViewById(R.id.profile_recycler, RecyclerView.class));
        StarAdapter post = new StarAdapter(stars);
        findViewById(R.id.profile_recycler, RecyclerView.class).setAdapter(post);

        if (stars.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
        }
        addDivider();
    }

    void bindUserTops(ArrayList<UserTop> userTops) {
        mRecyclerHelper.setItemViewSwipeEnable(false);
        mRecyclerHelper.detachFromRecyclerView(findViewById(R.id.profile_recycler, RecyclerView.class));
        UserWeeklyTopAdapter adapter = new UserWeeklyTopAdapter(userTops, mUser);
        findViewById(R.id.profile_recycler, RecyclerView.class).setAdapter(adapter);
        if (userTops.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
        }

        addDivider();
    }

    void addDivider() {
        if (findViewById(R.id.profile_recycler, RecyclerView.class).getItemDecorationCount() == 0) {

            DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider2));
            findViewById(R.id.profile_recycler, RecyclerView.class).addItemDecoration(divider);
            findViewById(R.id.profile_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    void removeDivider() {
        while (findViewById(R.id.profile_recycler, RecyclerView.class).getItemDecorationCount() > 0) {
            findViewById(R.id.profile_recycler, RecyclerView.class).removeItemDecorationAt(0);
        }
    }

    void visibleNoPosts() {
        findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.GONE);
        findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.VISIBLE);
    }


    void visibleRecycler() {
        findViewById(R.id.profile_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        findViewById(R.id.profile_no_post_text, AppCompatTextView.class).setVisibility(View.GONE);
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

    void setSelected(int position) {
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
        selectedIndex = 1;
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
                                userTops = response.data;
                                mView.onLoadedUserTops(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
