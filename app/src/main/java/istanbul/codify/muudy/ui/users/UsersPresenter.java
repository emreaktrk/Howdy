package istanbul.codify.muudy.ui.users;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.AroundUsersRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.AroundUsersResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Emotion;
import istanbul.codify.muudy.model.User;
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
}
