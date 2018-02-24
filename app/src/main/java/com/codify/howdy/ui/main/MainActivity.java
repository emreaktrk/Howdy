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
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.ui.compose.ComposeActivity;
import com.codify.howdy.ui.home.HomeFragment;
import com.codify.howdy.ui.notification.NotificationFragment;
import com.codify.howdy.ui.profile.ProfileFragment;
import com.codify.howdy.ui.statistic.StatisticFragment;


public final class MainActivity extends HowdyActivity implements MainView, Navigation.IController {

    private MainPresenter mPresenter = new MainPresenter();
    private Pool<Fragment> mPool = new Pool<Fragment>() {
        @Override
        protected Fragment supply(Class<? extends Fragment> clazz) throws Exception {
            return clazz.newInstance();
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStatisticClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(StatisticFragment.class))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onComposeClicked() {
        ComposeActivity.start();
    }

    @Override
    public void onNotificationClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(NotificationFragment.class))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onProfileClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(ProfileFragment.class))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNavigationSelected(@Navigation int selection) {
        mPresenter.setSelected(selection);
    }
}
