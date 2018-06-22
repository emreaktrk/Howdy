package istanbul.codify.muudy.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.ui.auth.AuthActivity;
import istanbul.codify.muudy.ui.main.MainActivity;

public final class SplashActivity extends MuudyActivity {

    private static final int DURATION = 1500;

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, SplashActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler()
                .postDelayed(() -> {
                    if (AccountUtils.has(SplashActivity.this)) {
                        MainActivity.start();
                    } else {
                        AuthActivity.start();
                    }
                }, DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
