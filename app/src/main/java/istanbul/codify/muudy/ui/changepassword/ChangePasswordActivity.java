package istanbul.codify.muudy.ui.changepassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ApiResult;
import istanbul.codify.muudy.model.Result;

public final class ChangePasswordActivity extends MuudyActivity implements ChangePasswordView {

    private ChangePasswordPresenter mPresenter = new ChangePasswordPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ChangePasswordActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_change_password;
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
    public void onChangeClicked() {
        mPresenter.changePassword();
    }

    @Override
    public void onLoaded(ApiResult result) {
        if (result.r == Result.OK) {
            Analytics
                    .getInstance()
                    .custom(Analytics.Events.CHANGE_PASSWORD);

            ToastUtils.showShort("Sifreniz degistirilmistir");
            finish();
        }
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Throwable throwable) {
        ToastUtils.showShort(throwable.getMessage());
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
