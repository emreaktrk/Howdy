package istanbul.codify.muudy.helper.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class CategoryLayoutManager extends GridLayoutManager {

    public CategoryLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanCount(3);
    }

    public CategoryLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        setSpanCount(3);
    }

    public CategoryLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        setSpanCount(3);
    }
}
