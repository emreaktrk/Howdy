package com.codify.howdy.model;

import android.support.annotation.IntDef;

@IntDef({ResultTo.ACTIVITY, ResultTo.FRAGMENT})
public @interface ResultTo {
    int ACTIVITY = 0;
    int FRAGMENT = 1;
}