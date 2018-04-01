package com.codify.howdy.ui.profileedit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.auth.AuthActivity;

public final class ProfileEditActivity extends HowdyActivity implements ProfileEditView {

    private ProfileEditPresenter mPresenter = new ProfileEditPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ProfileEditActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_profile_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        User me = AccountUtils.me(this);
        mPresenter.bind(me);
    }

    @Override
    public void onPhotoClicked() {
        mPresenter.selectPhoto(this);
    }

    @Override
    public void onPhotoSelected(Uri uri) {
        mPresenter.bind(uri);
    }

    @Override
    public void onSaveClicked() {

    }

    @Override
    public void onLoaded(String imagePath) {

    }

    @Override
    public void onLoaded(User user) {

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
