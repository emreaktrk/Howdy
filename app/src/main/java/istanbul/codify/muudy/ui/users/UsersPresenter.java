package istanbul.codify.muudy.ui.users;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
import org.json.JSONException;

import java.util.ArrayList;
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
                            Logcat.v("User searched : " + findViewById(R.id.users_search, AppCompatEditText.class).getText().toString());

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

    public void bind(@UsersScreenMode String mode) {
        findViewById(R.id.users_title, AppCompatTextView.class).setText(mode);
    }

    void getLikers(Long postId){
        SeeLikersRequest request = new SeeLikersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.postId = postId;

        mDisposables.add(
          ApiManager
                .getInstance()
                .seeLikers(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ServiceConsumer<SeeLikersResponse>() {
                    @Override
                    protected void success(SeeLikersResponse response) {
                        mView.onLoaded(response.data);
                    }

                    @Override
                    protected void error(ApiError error) {

                    }
                })
        );
    }

    void getContacts(Activity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.READ_CONTACTS)
                        .subscribe(granted -> {
                            if (granted) {
                                GetPhoneFriendsRequest request = new GetPhoneFriendsRequest(getPhoneBook(activity));
                                request.token = AccountUtils.tokenLegacy(getContext());

                                mDisposables.add(
                                        ApiManager
                                                .getInstance()
                                                .getPhoneFriends(request)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new ServiceConsumer<GetPhoneFriendsResponse>() {
                                                    @Override
                                                    protected void success(GetPhoneFriendsResponse response) {
                                                        mView.onLoaded(response.data);
                                                    }

                                                    @Override
                                                    protected void error(ApiError error) {
                                                        Logcat.e(error);

                                                        mView.onError(error);
                                                    }
                                                }));
                            }
                        })
        );
    }

    private ArrayList<String> getPhoneBook(Activity activity) {
        ArrayList<String> phones = new ArrayList<>();

        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor query = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (query != null && query.moveToNext()) {
                        String phone = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phones.add(phone);
                    }

                    if (query != null && !query.isClosed()) {
                        query.close();
                    }
                }
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return phones;
    }

    void getFollowedUsers(User user) {
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

    void getFollowers(User user) {
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

    void getFacebookFriends() {
        GraphRequest request =
                GraphRequest
                        .newGraphPathRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/friends",
                                response -> {
                                    try {
                                        String data = response.getJSONObject().get("data").toString();
                                        ArrayList<FacebookProfile> friends = new Gson().fromJson(data, new TypeToken<ArrayList<FacebookProfile>>() {
                                        }.getType());

                                        List<String> ids = new ArrayList<>();
                                        for (FacebookProfile profile : friends) {
                                            ids.add(profile.id);
                                        }

                                        requestFriends(ids);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mView.onError(new ApiError("Facebook a baglanilamadi"));
                                    }
                                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void requestFriends(List<String> ids) {
        FacebookFriendsRequest request = new FacebookFriendsRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.facebookIdArray = ids.toString().replace("[", "").replace("]", "");

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .facebookFriends(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<FacebookFriendsResponse>() {
                            @Override
                            protected void success(FacebookFriendsResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void update(FacebookProfile facebook) {
        // TODO Update profile with facebook id
    }
}
