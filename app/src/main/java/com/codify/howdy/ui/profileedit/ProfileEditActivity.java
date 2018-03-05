package com.codify.howdy.ui.profileedit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;

public final class ProfileEditActivity extends HowdyActivity implements ProfileEditView {

    private ProfileEditPresenter mPresenter = new ProfileEditPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_profile_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
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

    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
