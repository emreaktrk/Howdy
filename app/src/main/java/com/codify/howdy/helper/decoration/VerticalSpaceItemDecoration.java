package com.codify.howdy.helper.decoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mOffsetPx;

    public VerticalSpaceItemDecoration(int offsetPx) {
        this.mOffsetPx = offsetPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemCount = state.getItemCount();
        if (parent.getChildAdapterPosition(view) != itemCount - 1) {
            int orientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.right = mOffsetPx;
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.bottom = mOffsetPx;
            }
        }
    }
}