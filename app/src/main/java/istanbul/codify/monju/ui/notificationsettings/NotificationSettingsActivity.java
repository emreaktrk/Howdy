package istanbul.codify.monju.ui.notificationsettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.NotificationSettings;
import istanbul.codify.monju.model.User;

public final class NotificationSettingsActivity extends MuudyActivity implements NotificationSettingsView {

    private NotificationSettingsPresenter mPresenter = new NotificationSettingsPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, NotificationSettingsActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification_settings;
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
    public void onSettingsChanged(NotificationSettings settings) {
        mPresenter.update(settings);
    }

    @Override
    public void onLoaded() {
        AccountUtils.sync(this);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
