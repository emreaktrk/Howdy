package com.codify.howdy.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.ui.compose.ComposeActivity;
import com.codify.howdy.ui.home.HomeFragment;


public final class MainActivity extends HowdyActivity implements MainView {

    private MainPresenter mPresenter = new MainPresenter();

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, MainActivity.class);
        activity.startActivity(starter);

        activity.finish();
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
                .replace(R.id.home_frame, HomeFragment.newInstance())
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
        ComposeActivity.start(this);
    }

    @Override
    public void onNotificationClicked() {

    }

    @Override
    public void onProfileClicked() {

    }
}
