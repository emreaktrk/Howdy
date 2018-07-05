package istanbul.codify.muudy.ui.userprofile;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.*;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ViewSwitcher;
import com.binaryfork.spanny.Spanny;
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
import istanbul.codify.muudy.api.pojo.request.FollowRequest;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.api.pojo.response.FollowResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.home.PostAdapter;
import istanbul.codify.muudy.ui.profile.StarAdapter;
import istanbul.codify.muudy.ui.profile.UserWeeklyTopAdapter;
import istanbul.codify.muudy.view.FollowButton;
import istanbul.codify.muudy.view.NumberView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

final class UserProfilePresenter extends BasePresenter<UserProfileView> {

    private User mUser;
    private int selectedIndex = 0;

    public static int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            return 0;
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    @Override
    public void attachView(UserProfileView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_number_followed))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Followers clicked");

                            view.onFollowersClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_message))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Message clicked");

                            view.onMessageClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_muudy))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Muudy clicked");

                            view.onMuudyClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_number_followed))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Followers clicked");

                            view.onFollowersClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_number_following))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Followings clicked");

                            view.onFollowingsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_posts))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Posts clicked");

                            setSelected(0);
                            view.onPostsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_follow))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Follow clicked");

                            view.onFollowClicked(findViewById(R.id.user_profile_follow, FollowButton.class));
                        }));
        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_tops))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Tops clicked");

                            setSelected(1);
                            view.onTopsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_games))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Games clicked");

                            setSelected(2);
                            view.onGamesClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_series))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Series clicked");

                            setSelected(3);
                            view.onSeriesClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_films))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Films clicked");

                            setSelected(4);
                            view.onFilmsClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_books))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Books clicked");

                            setSelected(5);
                            view.onBooksClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_picture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture clicked");

                            view.onPictureClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_facebook))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Facebook clicked");

                            view.onFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_twitter))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Twitter clicked");

                            view.onTwitterClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_instagram))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Instagram clicked");

                            view.onInstagramClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_profile_more))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("More clicked");

                            view.onMoreClicked();
                        }));

        mDisposables.add(
                RxSwipeRefreshLayout
                        .refreshes(findViewById(R.id.user_profile_refresh))
                        .subscribe(o -> {
                            Logcat.v("Refresh clicked");

                            mView.onRefresh();
                        }));

        findViewById(R.id.user_profile_appbar, AppBarLayout.class)
                .addOnOffsetChangedListener((layout, offset) -> findViewById(R.id.user_profile_refresh).setEnabled(offset == 0));


    }

    void hideFollowView(){
        findViewById(R.id.user_profile_switcher, ViewSwitcher.class).setVisibility(View.GONE);
    }


    void addDiveder(){

        if(findViewById(R.id.user_profile_recycler, RecyclerView.class).getItemDecorationCount() == 0) {

            DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider2));
            findViewById(R.id.user_profile_recycler, RecyclerView.class).addItemDecoration(divider);
            findViewById(R.id.user_profile_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    void removeDiveder(){
        while (findViewById(R.id.user_profile_recycler, RecyclerView.class).getItemDecorationCount() > 0) {
            findViewById(R.id.user_profile_recycler, RecyclerView.class).removeItemDecorationAt(0);
        }
    }

    void    load(@NonNull Long userId, Boolean isForPosts) {
        GetUserProfileRequest request = new GetUserProfileRequest(userId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserProfile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                            @Override
                            protected void success(GetUserProfileResponse response) {
                                mView.onLoaded(response.data.user, isForPosts);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(User user) {
        mUser = user;

        if (user.iduser == AccountUtils.me(getContext()).iduser) {
            findViewById(R.id.user_profile_switcher, ViewSwitcher.class).setVisibility(View.GONE);
        }else{
            findViewById(R.id.user_profile_switcher, ViewSwitcher.class).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.user_profile_username, AppCompatTextView.class).setText(user.username);

        if (user.birtDate != null) {
            int age = getAge(user.birtDate);

            findViewById(R.id.user_profile_fullname, AppCompatTextView.class).setText(
                    new Spanny().append(user.namesurname + ", ", new StyleSpan(Typeface.BOLD)).append(age + ""));
        } else {
            findViewById(R.id.user_profile_fullname, AppCompatTextView.class).setText(user.namesurname);
        }

        switch (user.isfollowing) {
            case FOLLOWING:
                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.UNFOLLOW);
                showNext(user.isfollowing);
                break;
            case NOT_FOLLOWING:
                showNext(user.isfollowing);
                if (user.isprofilehidden == ProfileVisibility.HIDDEN) {
                    findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.REQUEST);
                } else {
                    findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.FOLLOW);
                }

                break;
            case REQUEST_SENT:
                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.REQUEST_CANCEL);
                showNext(user.isfollowing);
                break;
        }

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

        if (StringUtils.isEmpty(user.facebooklink) || !isVisibleProfile()) {
            findViewById(R.id.user_profile_facebook).setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(user.twitterlink) || !isVisibleProfile()) {
            findViewById(R.id.user_profile_twitter).setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(user.instagramlink) || !isVisibleProfile()) {
            findViewById(R.id.user_profile_instagram).setVisibility(View.GONE);
        }

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < awards.size(); i++) {
            text.append(awards.get(i));

            if (i != awards.size() - 1) {
                text.append(", ");
            }
        }
        findViewById(R.id.user_profile_award, AppCompatTextView.class).setText(text);

        NumberView posts = findViewById(R.id.user_profile_number_posts, NumberView.class);
        posts.setText("Paylaşım");
        posts.setValue(user.postcount);

        NumberView followed = findViewById(R.id.user_profile_number_followed, NumberView.class);
        followed.setText("Takip Eden");
        followed.setValue(user.followedcount);

        NumberView following = findViewById(R.id.user_profile_number_following, NumberView.class);
        following.setText("Takip Edilen");
        following.setValue(user.followercount);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.user_profile_picture, CircleImageView.class));

        if (isVisibleProfile()) {
            visibleRecycler();
        } else {
            visibleHiddenProfile();
        }
    }

    void refresh() {
        load(mUser.iduser, false);
        if (selectedIndex == 0) {
            posts();
        }else if (selectedIndex == 1 ) {
            tops();
        }else {
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
        if (mUser == null) {
            return;
        }

        setSelected(0);

        selectedIndex = 0;
        if (isVisibleProfile()) {
            GetUserProfileRequest request = new GetUserProfileRequest(mUser.iduser);
            request.token = AccountUtils.tokenLegacy(getContext());

            findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(true);

            mDisposables.add(
                    ApiManager
                            .getInstance()
                            .getUserProfile(request)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                                @Override
                                protected void success(GetUserProfileResponse response) {
                                    mView.onLoadedPosts(response.data.postlist);
                                    findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                }

                                @Override
                                protected void error(ApiError error) {
                                    Logcat.e(error);
                                    findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                    mView.onError(error);
                                }
                            }));
            visibleRecycler();

        } else {
            findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
            visibleHiddenProfile();
        }
    }

    void bindPosts(List<Post> posts) {
        removeDiveder();
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
                        .muudyClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Muudy clicked");

                            mView.onMuudyClicked(cell);
                        }));
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setAdapter(post);

        if (posts.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
        }

    }

    void stars(long categoryId) {
        if (mUser == null) {
            return;
        }

        if (isVisibleProfile()) {
            if (categoryId == Category.GAME) {
                selectedIndex = 2;
            } else if (categoryId == Category.SERIES) {
                selectedIndex = 3;
            } else if (categoryId == Category.FILM) {
                selectedIndex = 4;
            } else if (categoryId == Category.BOOK) {
                selectedIndex = 5;
            }

            findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(true);

            GetUserPostsRequest request = new GetUserPostsRequest(mUser.iduser);
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
                                    mView.onLoadedStars(response.data);
                                    findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                }

                                @Override
                                protected void error(ApiError error) {
                                    Logcat.e(error);
                                    findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                    mView.onError(error);
                                }
                            }));

            visibleRecycler();
        } else {
            findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
            visibleHiddenProfile();
        }
    }

    public boolean isVisibleProfile() {
        if (mUser == null) {
            return false;
        }

        User me = AccountUtils.me(getContext());
        if (me.iduser == mUser.iduser){
            return true;
        }else {
            if (mUser.isprofilehidden == ProfileVisibility.HIDDEN) {
                if (mUser.isfollowing == FollowState.FOLLOWING) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    void visibleNoPosts() {
        findViewById(R.id.user_profile_hidden_container).setVisibility(View.GONE);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setVisibility(View.GONE);
        findViewById(R.id.user_profile_no_post_text, AppCompatTextView.class).setVisibility(View.VISIBLE);
    }

    void visibleHiddenProfile() {
        findViewById(R.id.user_profile_hidden_container).setVisibility(View.VISIBLE);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setVisibility(View.GONE);
        findViewById(R.id.user_profile_no_post_text, AppCompatTextView.class).setVisibility(View.GONE);
    }

    void visibleRecycler() {
        findViewById(R.id.user_profile_hidden_container).setVisibility(View.GONE);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setVisibility(View.VISIBLE);
        findViewById(R.id.user_profile_no_post_text, AppCompatTextView.class).setVisibility(View.GONE);
    }

    void bindStars(List<Post> stars) {
        StarAdapter post = new StarAdapter(stars);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setAdapter(post);
        if (stars.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
        }
        addDiveder();
    }

    void tops() {
        selectedIndex = 1;
        if (isVisibleProfile()) {
            GetUserWeeklyTopRequest request = new GetUserWeeklyTopRequest(mUser.iduser);
            request.token = AccountUtils.tokenLegacy(getContext());

            mDisposables.add(
                    ApiManager
                            .getInstance()
                            .getUserWeeklyTop(request)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ServiceConsumer<GetUserWeeklyTopResponse>() {
                                @Override
                                protected void success(GetUserWeeklyTopResponse response) {
                                    mView.onLoadedUserTops(response.data);
                                }

                                @Override
                                protected void error(ApiError error) {
                                    Logcat.e(error);

                                    mView.onError(error);
                                }
                            }));
        }else{
            findViewById(R.id.user_profile_refresh, SwipeRefreshLayout.class).setRefreshing(false);
            visibleHiddenProfile();
        }
    }

    void bindUserTops(ArrayList<UserTop> userTops){
        UserWeeklyTopAdapter adapter = new UserWeeklyTopAdapter(userTops,mUser);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setAdapter(adapter);
        if (userTops.size() == 0) {
            visibleNoPosts();
        } else {
            visibleRecycler();
        }

        addDiveder();
    }

    User getUser() {
        return mUser;
    }

    private void setSelected(int position) {
        LinearLayoutCompat tabs = findViewById(R.id.user_profile_tabs, LinearLayoutCompat.class);
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
                                mView.onMuudySent();

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void follow() {
        FollowRequest request = new FollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.followtouserid = mUser.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .follow(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<FollowResponse>() {
                            @Override
                            protected void success(FollowResponse response) {
                                load(mUser.iduser, false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void unfollow() {
        UnfollowRequest request = new UnfollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.unfollowtouserid = mUser.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .unfollow(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<UnfollowResponse>() {
                            @Override
                            protected void success(UnfollowResponse response) {
                                //  findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.FOLLOW);
                                load(mUser.iduser, false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void requestFollow() {
        SendFollowRequest request = new SendFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userid = mUser.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sendFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendFollowResponse>() {
                            @Override
                            protected void success(SendFollowResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void cancelRequestFollow() {
        CancelFollowRequest request = new CancelFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.followRequestedUserId = mUser.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .cancelFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CancelFollowResponse>() {
                            @Override
                            protected void success(CancelFollowResponse response) {
                                load(mUser.iduser, false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void showNext(FollowState followState) {
        switch (followState) {
            case FOLLOWING:
                if ((findViewById(R.id.user_profile_switcher, ViewSwitcher.class).getNextView() instanceof LinearLayoutCompat)){
                    findViewById(R.id.user_profile_switcher, ViewSwitcher.class).showNext();
                }
                break;
            case NOT_FOLLOWING:
                if ((findViewById(R.id.user_profile_switcher, ViewSwitcher.class).getNextView() instanceof FollowButton)){
                    findViewById(R.id.user_profile_switcher, ViewSwitcher.class).showNext();
                }
                break;
            case REQUEST_SENT:
                if ((findViewById(R.id.user_profile_switcher, ViewSwitcher.class).getNextView() instanceof FollowButton)){
                    findViewById(R.id.user_profile_switcher, ViewSwitcher.class).showNext();
                }
                break;
        }
    }

    void block(boolean isBlocked) {
        BanUserRequest request = new BanUserRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.otherUserId = mUser.iduser;
        request.willBeBanned = isBlocked ? 1 : 0;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .banUser(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<BanUserResponse>() {
                            @Override
                            protected void success(BanUserResponse response) {
                                if (response.data.r == Result.OK) {
                                    mUser.isbanned = isBlocked ? 1 : 0;
                                }
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void notification(boolean isEnable) {
        if (isEnable) {
            SaveSendNotificationOnPostRequest request = new SaveSendNotificationOnPostRequest();
            request.token = AccountUtils.tokenLegacy(getContext());
            request.otherUserId = mUser.iduser;

            mDisposables.add(
                    ApiManager
                            .getInstance()
                            .saveSendNotificationOnPost(request)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ServiceConsumer<SaveSendNotificationOnPostResponse>() {
                                @Override
                                protected void success(SaveSendNotificationOnPostResponse response) {
                                    if (response.data.r == Result.OK) {
                                        // TODO Change push settings on user
                                    }
                                }

                                @Override
                                protected void error(ApiError error) {
                                    Logcat.e(error);

                                    mView.onError(error);
                                }
                            }));
        } else {
            DeleteSendNotificationOnPostRequest request = new DeleteSendNotificationOnPostRequest();
            request.token = AccountUtils.tokenLegacy(getContext());
            request.otherUserId = mUser.iduser;

            mDisposables.add(
                    ApiManager
                            .getInstance()
                            .deleteSendNotificationOnPost(request)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ServiceConsumer<DeleteSendNotificationOnPostResponse>() {
                                @Override
                                protected void success(DeleteSendNotificationOnPostResponse response) {
                                    if (response.data.r == Result.OK) {
                                        // TODO Change push settings on user
                                    }
                                }

                                @Override
                                protected void error(ApiError error) {
                                    Logcat.e(error);

                                    mView.onError(error);
                                }
                            }));
        }
    }

    void report() {
        ReportRequest request = new ReportRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.itemId = mUser.iduser;
        request.type = ReportType.USER;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .report(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<ReportResponse>() {
                            @Override
                            protected void success(ReportResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }




}
