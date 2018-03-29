package com.codify.howdy.ui.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import com.appunite.appunitevideoplayer.PlayerActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.Post;

public final class VideoActivity extends PlayerActivity {

    public static void start(@NonNull String url) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = PlayerActivity.getVideoPlayerIntent(context, BuildConfig.URL + url, "");
        starter.setClass(context, VideoActivity.class);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull Post post) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = PlayerActivity.getVideoPlayerIntent(context, BuildConfig.URL + post.post_video_path, "");
        starter.setClass(context, VideoActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.button_back);
        if (drawable != null) {
            drawable.setTint(Color.WHITE);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(drawable);
        }
    }
}
