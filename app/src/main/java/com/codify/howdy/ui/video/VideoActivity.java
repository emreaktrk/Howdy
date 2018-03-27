package com.codify.howdy.ui.video;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.appunite.appunitevideoplayer.PlayerActivity;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.model.Post;

public final class VideoActivity extends PlayerActivity {

    public static void start(@NonNull String url) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = PlayerActivity.getVideoPlayerIntent(context, url, "");
        context.startActivity(starter);
    }

    public static void start(@NonNull Post post) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = PlayerActivity.getVideoPlayerIntent(context, BuildConfig.URL + post.post_video_path, "");
        context.startActivity(starter);
    }
}
