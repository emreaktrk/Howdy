package istanbul.codify.muudy.ui.userphotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.userphotos.photo.UserPhotoFragment;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;

public final class UserPhotosActivity extends MuudyActivity implements UserPhotosView {

    private UserPhotosPresenter mPresenter = new UserPhotosPresenter();

    public static void start(@NonNull User user) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UserPhotosActivity.class);
        starter.putExtra(user.getClass().getSimpleName(), user);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_photos;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.bind(user);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public FragmentPagerItemAdapter create(User user) {
        return new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems
                        .with(this)
                        .add("Resim 1", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath1))
                        .add("Resim 2", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath2))
                        .add("Resim 3", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath3))
                        .create());
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
