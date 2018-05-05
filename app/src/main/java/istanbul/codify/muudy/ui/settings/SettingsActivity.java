package istanbul.codify.muudy.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.ui.splash.SplashActivity;
import istanbul.codify.muudy.ui.web.WebActivity;

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
    public void onSettingsUpdated() {
        AccountUtils.sync(this);

        ToastUtils.showShort("Ayarlar guncellendi.");
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
