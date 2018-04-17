package istanbul.codify.muudy.analytics;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;

public final class FirebaseAnalyst implements IAnalyst {

    @Override
    public void custom(String event) {
        Context context = Utils.getApp().getApplicationContext();

        FirebaseAnalytics
                .getInstance(context)
                .logEvent(event, null);
    }
}
