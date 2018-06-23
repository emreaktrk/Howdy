package istanbul.codify.muudy.ui.profileedit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.changepassword.ChangePasswordActivity;
import istanbul.codify.muudy.ui.media.MediaBottomSheet;

public final class ProfileEditActivity extends MuudyActivity implements ProfileEditView {

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
    public void onPhotoClicked(CircleImageView view) {
        MediaBottomSheet
                .newInstance()
                .setOnCameraClickListener(() -> mPresenter.capturePhoto(ProfileEditActivity.this, view))
                .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ProfileEditActivity.this, view))
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPhoto2Clicked(CircleImageView view, User user) {
        if (user.imgpath2 != null) {
            MediaBottomSheet
                    .newInstance(user)
                    .setOnCameraClickListener(() -> mPresenter.capturePhoto(ProfileEditActivity.this, view))
                    .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ProfileEditActivity.this, view))
                    .setOnMakeProfileImageListener((mUser) -> mPresenter.makeSecondPhotoProfileImage(mUser))
                    .show(getSupportFragmentManager(), null);
        } else {
            MediaBottomSheet
                    .newInstance()
                    .setOnCameraClickListener(() -> mPresenter.capturePhoto(ProfileEditActivity.this, view))
                    .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ProfileEditActivity.this, view))
                    .show(getSupportFragmentManager(), null);
        }
    }


    @Override
    public void onPhoto3Clicked(CircleImageView view, User user) {
        if (user.imgpath3 != null) {
            MediaBottomSheet
                    .newInstance(user)
                    .setOnCameraClickListener(() -> mPresenter.capturePhoto(ProfileEditActivity.this, view))
                    .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ProfileEditActivity.this, view))
                    .setOnMakeProfileImageListener((mUser) -> mPresenter.makeThirdPhotoProfileImage(mUser))
                    .show(getSupportFragmentManager(), null);
        } else {
            MediaBottomSheet
                    .newInstance()
                    .setOnCameraClickListener(() -> mPresenter.capturePhoto(ProfileEditActivity.this, view))
                    .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ProfileEditActivity.this, view))
                    .show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onPhotoSelected(Uri uri, CircleImageView view) {
        mPresenter.bind(uri, view);
    }

    @Override
    public void onSaveClicked() {
        mPresenter.save();

        Analytics
                .getInstance()
                .custom(Analytics.Events.UPDATE_PROFILE);
    }

    @Override
    public void onChangePasswordClicked() {
        ChangePasswordActivity.start();
    }

    @Override
    public void onProfileUpdated() {
        AccountUtils.sync(this);

        ToastUtils.showShort("Profiliniz başarıyla güncellendi");
    }

    @Override
    public void onLoaded(String imagePath) {

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
