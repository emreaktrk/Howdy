package com.codify.howdy.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import com.blankj.utilcode.util.SizeUtils;

public final class LikeButton extends AppCompatCheckBox {

    public LikeButton(Context context) {
        super(context);

        init();
    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setPadding(
                getPaddingLeft() + SizeUtils.dp2px(2),
                getPaddingTop(),
                getPaddingRight(),
                getPaddingBottom());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text.length() > 0 && text.charAt(0) != ' ') {
            text = " " + text;
        }

        super.setText(text, type);
    }
}
