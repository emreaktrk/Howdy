package com.codify.howdy.ui.main;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class MainPresenter extends BasePresenter<MainView> {

    @Override
    public void attachView(MainView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.main_compose))
                        .subscribe(o -> {
                            Logcat.v("Compose clicked");
                            view.onComposeClicked();
                        }));
    }
}
