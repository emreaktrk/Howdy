package com.codify.howdy.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.helper.Pool;
import com.codify.howdy.ui.compose.ComposeActivity;
import com.codify.howdy.ui.home.HomeFragment;


public final class MainActivity extends HowdyActivity implements MainView {

    private MainPresenter mPresenter = new MainPresenter();
    private Pool<Fragment> mPool = new Pool<Fragment>() {
        @Override
        protected Fragment supply(Class<? extends Fragment> key) throws Exception {
            return key.newInstance();
        }
    };

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();
        Intent starter = new Intent(context, MainActivity.class);
        ActivityUtils.startActivity(starter);
        ActivityUtils.finishAllActivities();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                .commit();

        mPresenter.attachView(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onHomeClicked() {

    }

    @Override
    public void onStatisticClicked() {

    }

    @Override
    public void onComposeClicked() {
        ComposeActivity.start();
    }

    @Override
    public void onNotificationClicked() {

    }

    @Override
    public void onProfileClicked() {

    }
}
