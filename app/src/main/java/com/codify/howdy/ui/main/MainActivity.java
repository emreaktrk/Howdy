package com.codify.howdy.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.ui.landing.LandingFragment;

public final class MainActivity extends HowdyActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, LandingFragment.newInstance())
                .commit();
    }
}
