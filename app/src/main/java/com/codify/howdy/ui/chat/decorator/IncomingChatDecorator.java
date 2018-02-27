package com.codify.howdy.ui.chat.decorator;

import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.codify.howdy.R;
import com.codify.howdy.helper.Decorator;

public class IncomingChatDecorator extends Decorator<View> {

    public IncomingChatDecorator(View object) {
        super(object);
    }

    @Override
    protected void decorate(View object) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) object.getLayoutParams();
        params.gravity = Gravity.START;

        object.setBackgroundResource(R.drawable.background_chat_incoming);

        if (object instanceof AppCompatTextView) {
            AppCompatTextView text = (AppCompatTextView) object;
            text.setTextColor(Color.WHITE);
        }
    }
}
