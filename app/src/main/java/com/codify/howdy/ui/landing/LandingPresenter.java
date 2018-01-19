package com.codify.howdy.ui.landing;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class LandingPresenter extends BasePresenter<LandingView> {

    @Override
    public void attachView(LandingView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.landing_login))
                        .subscribe(o -> {
                            Logcat.v("Login clicked");
                            view.onLoginClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(root.findViewById(R.id.landing_register))
                        .subscribe(o -> {
                            Logcat.v("Register clicked");
                            view.onRegisterClicked();
                        }));
    }
}
