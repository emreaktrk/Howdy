package com.codify.howdy.ui.home;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

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
                        .clicks(findViewById(R.id.home_chat))
                        .subscribe(o -> {
                            Logcat.v("Chat clicked");
                            view.onChatClicked();
                        }));
    }
}
