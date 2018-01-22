package com.codify.howdy.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.ui.home.HomeFragment;


public final class MainActivity extends HowdyActivity implements MainView {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
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
    }

    @Override
    public void onHomeClicked() {

    }

    @Override
    public void onStatisticClicked() {

    }

    @Override
    public void onComposeClicked() {

    }

    @Override
    public void onNotificationClicked() {

    }

    @Override
    public void onProfileClicked() {

    }
}
