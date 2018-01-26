package com.codify.howdy.logcat;

import android.support.annotation.NonNull;

abstract class AbstractLog implements ILog {

    String mLog;

    AbstractLog(@NonNull String log) {
        mLog = log;
    }
}
