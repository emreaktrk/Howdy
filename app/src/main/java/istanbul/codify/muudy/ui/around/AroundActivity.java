package istanbul.codify.muudy.ui.around;

import android.content.Context;
import android.content.Intent;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.HowdyActivity;
import istanbul.codify.muudy.R;

public final class AroundActivity extends HowdyActivity {

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, AroundActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_around;
    }
}
