package com.codify.howdy.ui.facebookfriends;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class FacebookFriendsPresenter extends BasePresenter<FacebookFriendsView> {

    @Override
    public void attachView(FacebookFriendsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.facebook_friends_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));
    }

    void getUsers() {

    }
}
