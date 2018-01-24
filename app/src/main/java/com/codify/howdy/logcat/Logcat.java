package com.codify.howdy.logcat;

import android.util.Log;

import com.codify.howdy.api.pojo.response.ApiError;

public final class Logcat {

    private static final String TAG = "HOWDY";

    public static void v(String message) {
        Log.v(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void e(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(ApiError error) {
        Log.e(TAG, error.message);
    }
}
