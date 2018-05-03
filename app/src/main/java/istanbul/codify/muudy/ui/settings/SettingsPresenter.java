package istanbul.codify.muudy.ui.settings;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.LogoutRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.LogoutResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Settings;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class SettingsPresenter extends BasePresenter<SettingsView> {

    @Override
    public void attachView(SettingsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.settings_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.settings_logout))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Logout clicked");

                            view.onLogoutClicked();
                        }));
    }

    void update(Settings settings) {

    }

    void logout() {
        LogoutRequest request = new LogoutRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables
                .add(
                        ApiManager
                                .getInstance()
                                .logout(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ServiceConsumer<LogoutResponse>() {
                                    @Override
                                    protected void success(LogoutResponse response) {
                                        Logcat.v("Logout done");

                                        mView.onLogout(response.data);
                                    }

                                    @Override
                                    protected void error(ApiError error) {
                                        Logcat.e(error);

                                        mView.onError(error);
                                    }
                                })
                );
    }
}
