package istanbul.codify.muudy.ui.users;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Emotion;
import istanbul.codify.muudy.model.FollowState;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.UsersScreenMode;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

final class UsersPresenter extends BasePresenter<UsersView> {

    @Override
    public void attachView(UsersView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.users_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.users_search))
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .skip(1)
                        .subscribe(word -> {
                            Logcat.v("User searched : " + findViewById(R.id.search_search, AppCompatEditText.class).getText().toString());

                            view.onUserSearched(word.toString());
                        }));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.users_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.users_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void bind(List<User> users) {
        UsersAdapter adapter = new UsersAdapter(users);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Logcat.v("User clicked");

                            mView.onUserClicked(user);
                        }));
        mDisposables.add(
                adapter
                        .followClicks()
                        .subscribe(follow -> mView.onFollowClicked(follow)));
        findViewById(R.id.users_recycler, RecyclerView.class).setAdapter(adapter);
    }

    @SuppressLint("MissingPermission")
    void aroundUsers(Emotion emotion) {
        LocationServices
                .getFusedLocationProviderClient(getContext())
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
                .addOnSuccessListener(location -> {
                    AroundUsersRequest request = new AroundUsersRequest();
                    request.token = AccountUtils.tokenLegacy(getContext());
                    request.duyguId = emotion.idwords;
                    request.lat = location.getLatitude();
                    request.lng = location.getLongitude();

                    mDisposables.add(
                            ApiManager
                                    .getInstance()
                                    .aroundUsers(request)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ServiceConsumer<AroundUsersResponse>() {
                                        @Override
                                        protected void success(AroundUsersResponse response) {
                                            mView.onLoaded(response.data);
                                        }

                                        @Override
                                        protected void error(ApiError error) {
                                            Logcat.e(error);

                                            mView.onError(error);
                                        }
                                    }));
                })
                .addOnFailureListener(Logcat::e);
    }

    public void bind(User user, @UsersScreenMode String mode) {
        findViewById(R.id.users_title, AppCompatTextView.class).setText(mode);

        if (StringUtils.equals(mode, UsersScreenMode.FOLLOWER)) {
            getFollowedUsers(user);
        } else if (StringUtils.equals(mode, UsersScreenMode.FOLLOWING)) {
            getFollowers(user);
        }
    }

    private void getFollowedUsers(User user) {
        GetFollowedUsersRequest request = new GetFollowedUsersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = user.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getFollowedUsers(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetFollowedUsersResponse>() {
                            @Override
                            protected void success(GetFollowedUsersResponse response) {
                                for (User user : response.data) {
                                    user.isfollowing = FollowState.FOLLOWING;
                                }

                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    private void getFollowers(User user) {
        GetFollowersRequest request = new GetFollowersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = user.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getFollowers(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetFollowersResponse>() {
                            @Override
                            protected void success(GetFollowersResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void follow(User user) {
        FollowRequest request = new FollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.followtouserid = user.iduser;

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

    void unfollow(User user) {
        UnfollowRequest request = new UnfollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.unfollowtouserid = user.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .unfollow(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<UnfollowResponse>() {
                            @Override
                            protected void success(UnfollowResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void requestFollow(User user) {
        SendFollowRequest request = new SendFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userid = user.iduser;

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

    void cancelRequestFollow(User user) {
        CancelFollowRequest request = new CancelFollowRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.followRequestedUserId = user.iduser;

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
}
