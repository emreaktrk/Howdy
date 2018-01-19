package com.codify.howdy;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public abstract class HowdyActivity extends AppCompatActivity {

    abstract protected @LayoutRes
    int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
    }
}
