package istanbul.codify.muudy.ui.notificationsettings;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.UpdatePushSettingsRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.UpdatePushNotificationResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.NotificationSettings;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

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
    }

    void bind(User me) {
        findViewById(R.id.notification_settings_follow, SwitchCompat.class).setChecked(me.push_on_follow == 1);
        findViewById(R.id.notification_settings_like, SwitchCompat.class).setChecked(me.push_on_like == 1);
        findViewById(R.id.notification_settings_tag, SwitchCompat.class).setChecked(me.push_on_tag == 1);
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
