package istanbul.codify.muudy.ui.landing;

import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class LandingPresenter extends BasePresenter<LandingView> {

    @Override
    public void attachView(LandingView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.landing_login))
                        .subscribe(o -> {
                            Logcat.v("Login clicked");
                            view.onLoginClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.landing_register))
                        .subscribe(o -> {
                            Logcat.v("Register clicked");
                            view.onRegisterClicked();
                        }));
    }
}
