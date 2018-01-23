package com.codify.howdy.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.ui.auth.AuthActivity;
import com.codify.howdy.ui.main.MainActivity;

public final class SplashActivity extends HowdyActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthActivity.start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
