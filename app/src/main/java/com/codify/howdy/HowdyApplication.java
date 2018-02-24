package com.codify.howdy;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.codify.howdy.analytics.Analytics;
import com.codify.howdy.analytics.FabricAnalyst;
import com.codify.howdy.analytics.FirebaseAnalyst;
import com.google.firebase.FirebaseApp;


public final class HowdyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

        FirebaseApp.initializeApp(this);
        Analytics
                .getInstance()
                .add(new FirebaseAnalyst())
                .add(new FabricAnalyst());
    }
}
