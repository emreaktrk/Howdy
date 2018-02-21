package com.codify.howdy;

import android.app.Application;

import com.blankj.utilcode.util.Utils;


public final class HowdyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
