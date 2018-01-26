package com.codify.howdy.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

public class VerboseLog extends AbstractLog {

    VerboseLog(@NonNull String log) {
        super(log);
    }

    @Override
    public void display() {
        Log.v(TAG, mLog);
    }
}
