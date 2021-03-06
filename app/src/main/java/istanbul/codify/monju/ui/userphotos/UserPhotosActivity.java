package istanbul.codify.monju.ui.userphotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.userphotos.photo.UserPhotoFragment;

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
        FragmentPagerItems.Creator creator = FragmentPagerItems
                .with(this)
                .add("Resim 1", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath1));

        if (!StringUtils.isEmpty(user.imgpath2)) {
            creator.add("Resim 2", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath2));
        }

        if (!StringUtils.isEmpty(user.imgpath3)) {
            creator.add("Resim 3", UserPhotoFragment.class, UserPhotoFragment.args(user.imgpath3));
        }

        return new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
