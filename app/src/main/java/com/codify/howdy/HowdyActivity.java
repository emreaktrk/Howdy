package com.codify.howdy;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public abstract class HowdyActivity extends AppCompatActivity {

    public static final int NO_ID = -1;

    abstract protected @LayoutRes
    int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutResId() != NO_ID) {
            setContentView(getLayoutResId());
        }
    }
}
