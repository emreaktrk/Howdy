package istanbul.codify.muudy.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ActivityUtils;

public final class AndroidUtils {

    public static void browser(@NonNull String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        Activity activity = ActivityUtils.getTopActivity();
        activity.startActivity(browserIntent);
    }
}
