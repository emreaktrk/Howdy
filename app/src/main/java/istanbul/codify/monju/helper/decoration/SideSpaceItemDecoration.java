package istanbul.codify.monju.helper.decoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public final class SideSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mOffsetPx;
    private final int mOrientation;

    public SideSpaceItemDecoration(int offsetPx, int orientation) {
        this.mOffsetPx = offsetPx;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        switch (mOrientation) {
            case LinearLayoutManager.VERTICAL:
                outRect.top = mOffsetPx;
                outRect.bottom = mOffsetPx;
                return;
            case LinearLayoutManager.HORIZONTAL:
                outRect.left = mOffsetPx;
                outRect.right = mOffsetPx;
                return;
            default:
                throw new IllegalArgumentException("Unknown orientation");
        }
    }
}