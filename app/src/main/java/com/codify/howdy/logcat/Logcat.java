package com.codify.howdy.logcat;

import android.util.Log;

public final class Logcat {

    private static final String TAG = "HOWDY";

    public static void v(String message) {
        Log.v(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }
}
