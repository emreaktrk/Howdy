package istanbul.codify.muudy.ui.settings;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxSeekBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.LogoutRequest;
import istanbul.codify.muudy.api.pojo.request.UpdateProfileRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.LogoutResponse;
import istanbul.codify.muudy.api.pojo.response.UpdateProfileResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.ProfileVisibility;
import istanbul.codify.muudy.model.Settings;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class SettingsPresenter extends BasePresenter<SettingsView> {

    @Override
    public void attachView(SettingsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.settings_policy))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Policy clicked");

                            view.onPolicyClicked();
                        }));

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

        mDisposables.add(
                RxSeekBar
                        .userChanges(findViewById(R.id.settings_distance))
                        .skipInitialValue()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(distance -> {
                            Logcat.v("Distance changed");

                            Settings settings = new Settings(distance);
                            view.onSettingsChanged(settings);
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.settings_hidden_profile))
                        .skipInitialValue()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hidden -> {
                            Logcat.v("Hidden profile changed");

                            Settings settings = new Settings(hidden ? ProfileVisibility.HIDDEN : ProfileVisibility.VISIBLE);
                            view.onSettingsChanged(settings);
                        }));
    }

    void update(Settings settings) {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.isprofilehidden = settings.profileVisibility;
        request.distance_km = settings.distance;

        mDisposables
                .add(
                        ApiManager
                                .getInstance()
                                .updateProfile(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ServiceConsumer<UpdateProfileResponse>() {
                                    @Override
                                    protected void success(UpdateProfileResponse response) {
                                        Logcat.v("Settings updated");

                                        mView.onSettingsUpdated();
                                    }

                                    @Override
                                    protected void error(ApiError error) {
                                        Logcat.e(error);

                                        mView.onError(error);
                                    }
                                })
                );
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

    void bind(User me) {
        findViewById(R.id.settings_distance, SeekBar.class).setProgress(me.distance_km);
        findViewById(R.id.settings_hidden_profile, SwitchCompat.class).setChecked(me.isprofilehidden != null && me.isprofilehidden == ProfileVisibility.HIDDEN);
    }
}
