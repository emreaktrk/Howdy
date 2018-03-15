package com.codify.howdy.ui.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;

public final class ForgotPasswordActivity extends HowdyActivity implements ForgotPasswordView {

    private ForgotPasswordPresenter mPresenter = new ForgotPasswordPresenter();

    public static void start(@Nullable String email) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ForgotPasswordActivity.class);
        if (!StringUtils.isEmpty(email)) {
            starter.putExtra(email.getClass().getSimpleName(), email);
        }
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_forgot_password;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        String email = getSerializable(String.class);
        mPresenter.bind(email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked() {
        mPresenter.sendEmail();
    }

    @Override
    public void onLoaded(String data) {
        ToastUtils.showShort(data);

        finish();
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
