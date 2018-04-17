package istanbul.codify.muudy.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import istanbul.codify.muudy.HowdyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.ui.auth.AuthActivity;
import istanbul.codify.muudy.ui.main.MainActivity;

public final class SplashActivity extends HowdyActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AccountUtils.has(this)) {
            MainActivity.start();
        } else {
            AuthActivity.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
