package istanbul.codify.monju.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.analytics.Analytics;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.ui.changepassword.ChangePasswordActivity;
import istanbul.codify.monju.ui.feedback.FeedbackActivity;
import istanbul.codify.monju.ui.notificationsettings.NotificationSettingsActivity;
import istanbul.codify.monju.ui.profileedit.ProfileEditActivity;
import istanbul.codify.monju.ui.socialmedia.SocialMediaActivity;
import istanbul.codify.monju.ui.splash.SplashActivity;
import istanbul.codify.monju.ui.users.UsersActivity;
import istanbul.codify.monju.ui.web.WebActivity;

public final class SettingsActivity extends MuudyActivity implements SettingsView {

    private SettingsPresenter mPresenter = new SettingsPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, SettingsActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_settings;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        User me = AccountUtils.me(this);
        mPresenter.bind(me);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onPolicyClicked() {
        WebActivity.start(new Web("Gizlilik Sözleşmesi", URL.POLICY));
    }

    @Override
    public void onFeedbackClicked() {
        FeedbackActivity.start();
    }

    @Override
    public void onContactsClicked() {
        UsersActivity.start(UsersScreenMode.CONTACTS);
    }

    @Override
    public void onFacebookFriendsClicked() {
        UsersActivity.start(UsersScreenMode.FACEBOOK);
    }

    @Override
    public void onSocialMediaClicked() {
        SocialMediaActivity.start();
    }

    @Override
    public void onChangePasswordClicked() {
        ChangePasswordActivity.start();
    }

    @Override
    public void onNotificationSettingsClicked() {
        NotificationSettingsActivity.start();
    }

    @Override
    public void onSettingsChanged(Settings settings) {
        mPresenter.update(settings);

        Analytics
                .getInstance()
                .custom(Analytics.Events.UPDATE_SETTINGS);
    }

    @Override
    public void onEditClicked() {
        ProfileEditActivity.start();
    }

    @Override
    public void onLogoutClicked() {
        new AlertDialog
                .Builder(this)
                .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                .setPositiveButton("Evet", (dialogInterface, which) -> {
                    AccountUtils.logout(this, future -> {
                        if (future) {
                            SplashActivity.start();
                            ActivityUtils.finishOtherActivities(SplashActivity.class);
                        }
                    });
                    mPresenter.logout();

                    Analytics
                            .getInstance()
                            .custom(Analytics.Events.LOGOUT);
                })
                .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onLogout(ApiResult result) {
        if (result.r == Result.OK) {
            /*AccountUtils.logout(this, future -> {
                if (future) {
                    SplashActivity.start();
                    ActivityUtils.finishOtherActivities(SplashActivity.class);
                }
            });*/
        }
    }

    @Override
    public void onSettingsUpdated() {
        AccountUtils.sync(this);

        ToastUtils.showShort("Ayarlar güncellendi.");
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
