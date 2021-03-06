package istanbul.codify.monju.ui.welcome;

import android.view.View;

import istanbul.codify.monju.R;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class WelcomePresenter extends BasePresenter<WelcomeView> {

    @Override
    public void attachView(WelcomeView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.welcome_register_with_email))
                        .subscribe(o -> {
                            Logcat.v("Register with email clicked");
                            view.onRegisterWithEmailClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.welcome_register_with_facebook))
                        .subscribe(o -> {
                            Logcat.v("Register with facebook clicked");
                            view.onRegisterWithFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.welcome_login))
                        .subscribe(o -> {
                            Logcat.v("Login clicked");
                            view.onLoginClicked();
                        }));
    }
}
