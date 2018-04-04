package com.codify.howdy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public final class FollowButton extends ToggleButton {

    public FollowButton(Context context) {
        super(context);

        init();
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setText(isChecked() ? "Takip Edildi" : "Takip Et");
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        setText(checked ? "Takip Edildi" : "Takip Et");
    }
}
