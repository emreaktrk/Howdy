package com.codify.howdy.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.request.GetWallRequest;
import com.codify.howdy.api.pojo.response.GetWallResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Wall;
import com.codify.howdy.ui.base.BasePresenter;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    }
}
