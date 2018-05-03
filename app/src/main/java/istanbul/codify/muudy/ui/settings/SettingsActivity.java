package istanbul.codify.muudy.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ApiResult;
import istanbul.codify.muudy.model.Result;
import istanbul.codify.muudy.model.Settings;
import istanbul.codify.muudy.ui.splash.SplashActivity;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSettingsChanged(Settings settings) {
        mPresenter.update(settings);
    }

    @Override
    public void onLogoutClicked() {
        mPresenter.logout();
    }

    @Override
    public void onLogout(ApiResult result) {
        if (result.r == Result.OK) {
            AccountUtils.logout(this, future -> {
                if (future) {
                    SplashActivity.start();
                    ActivityUtils.finishOtherActivities(SplashActivity.class);
                }
            });
        }
    }

    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
