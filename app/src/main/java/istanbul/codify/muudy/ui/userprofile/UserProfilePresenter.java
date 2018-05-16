package istanbul.codify.muudy.ui.userprofile;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.blankj.utilcode.util.StringUtils;
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
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.ReportType;
import istanbul.codify.muudy.model.Result;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.home.PostAdapter;
import istanbul.codify.muudy.ui.profile.StarAdapter;
import istanbul.codify.muudy.view.FollowButton;
import istanbul.codify.muudy.view.NumberView;

import java.util.ArrayList;
import java.util.List;

final class UserProfilePresenter extends BasePresenter<UserProfileView> {

    private User mUser;

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
    }

    void load(@NonNull Long userId) {
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
                                mView.onLoaded(response.data.user);
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

        findViewById(R.id.user_profile_username, AppCompatTextView.class).setText(user.username);
        findViewById(R.id.user_profile_fullname, AppCompatTextView.class).setText(user.namesurname);

        switch (user.isfollowing) {
            case FOLLOWING:
                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.UNFOLLOW);
                showNext();
                break;
            case NOT_FOLLOWING:
                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.FOLLOW);
                break;
            case REQUEST_SENT:
                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.REQUEST_CANCEL);
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

        if (StringUtils.isEmpty(user.facebooklink)) {
            findViewById(R.id.user_profile_facebook).setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(user.twitterlink)) {
            findViewById(R.id.user_profile_twitter).setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(user.instagramlink)) {
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
    }

    void posts() {
        if (mUser == null) {
            return;
        }

        setSelected(0);

        GetUserProfileRequest request = new GetUserProfileRequest(mUser.iduser);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserProfile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                            @Override
                            protected void success(GetUserProfileResponse response) {
                                mView.onLoadedPosts(response.data.postlist);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bindPosts(List<Post> posts) {
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
    }

    void stars(long categoryId) {
        if (mUser == null) {
            return;
        }

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
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bindStars(List<Post> stars) {
        StarAdapter post = new StarAdapter(stars);
        findViewById(R.id.user_profile_recycler, RecyclerView.class).setAdapter(post);
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
                                Toast.makeText(getContext(), "Muudy de başarıyla yollandı!", Toast.LENGTH_SHORT).show();
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
                                findViewById(R.id.user_profile_follow, FollowButton.class).setState(FollowButton.State.FOLLOW);
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

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void showNext() {
        findViewById(R.id.user_profile_switcher, ViewSwitcher.class).showNext();
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
