package istanbul.codify.monju.helper.decoration;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public final class LineItemDecoration extends android.support.v7.widget.DividerItemDecoration {


    public LineItemDecoration(Context context, int orientation, @ColorInt int color) {
        super(context, orientation);

        setDrawable(new ColorDrawable(color));
    }

    public LineItemDecoration(Context context, int orientation, @NonNull Drawable drawable) {
        super(context, orientation);

        setDrawable(drawable);
    }
}