package istanbul.codify.monju.ui.notificationsettings;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.UpdatePushSettingsRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.UpdatePushNotificationResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.NotificationSettings;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.BasePresenter;
import istanbul.codify.monju.utils.SharedPrefs;

import java.util.concurrent.TimeUnit;

final class NotificationSettingsPresenter extends BasePresenter<NotificationSettingsView> {

    @Override
    public void attachView(NotificationSettingsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.notification_settings_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.notification_settings_follow))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> {
                            Logcat.v("Follow notification changed");

                            NotificationSettings settings = new NotificationSettings();
                            settings.onFollow = findViewById(R.id.notification_settings_follow, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onLike = findViewById(R.id.notification_settings_like, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onTag = findViewById(R.id.notification_settings_tag, SwitchCompat.class).isChecked() ? 1 : 0;
                            view.onSettingsChanged(settings);
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.notification_settings_like))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> {
                            Logcat.v("Like notification changed");

                            NotificationSettings settings = new NotificationSettings();
                            settings.onFollow = findViewById(R.id.notification_settings_follow, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onLike = findViewById(R.id.notification_settings_like, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onTag = findViewById(R.id.notification_settings_tag, SwitchCompat.class).isChecked() ? 1 : 0;
                            view.onSettingsChanged(settings);
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.notification_settings_tag))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> {
                            Logcat.v("Tag notification changed");

                            NotificationSettings settings = new NotificationSettings();
                            settings.onFollow = findViewById(R.id.notification_settings_follow, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onLike = findViewById(R.id.notification_settings_like, SwitchCompat.class).isChecked() ? 1 : 0;
                            settings.onTag = findViewById(R.id.notification_settings_tag, SwitchCompat.class).isChecked() ? 1 : 0;
                            view.onSettingsChanged(settings);
                        }));

        mDisposables.add(
                RxCompoundButton
                        .checkedChanges(findViewById(R.id.notification_settings_location))
                        .skipInitialValue()
                        .skip(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> {
                            Logcat.v("Tag notification changed");

                            SharedPrefs.setPlaceRecommendationNotificationPermission(findViewById(R.id.notification_settings_location, SwitchCompat.class).isChecked(),getContext());

                        }));
    }

    void bind(User me) {
        findViewById(R.id.notification_settings_follow, SwitchCompat.class).setChecked(me.push_on_follow == 1);
        findViewById(R.id.notification_settings_like, SwitchCompat.class).setChecked(me.push_on_like == 1);
        findViewById(R.id.notification_settings_tag, SwitchCompat.class).setChecked(me.push_on_tag == 1);
        findViewById(R.id.notification_settings_location, SwitchCompat.class).setChecked(SharedPrefs.getPlaceRecommendationPermissionStatus(getContext()));

    }

    void update(NotificationSettings settings) {
        UpdatePushSettingsRequest request = new UpdatePushSettingsRequest(settings);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .updatePushSettings(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<UpdatePushNotificationResponse>() {
                            @Override
                            protected void success(UpdatePushNotificationResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
