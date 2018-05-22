package istanbul.codify.muudy.ui.landing;

import android.support.v4.view.ViewPager;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.ui.base.BasePresenter;

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

        ViewPager pager = findViewById(R.id.landing_pager, ViewPager.class);
        pager.setAdapter(view.create());
    }
}
