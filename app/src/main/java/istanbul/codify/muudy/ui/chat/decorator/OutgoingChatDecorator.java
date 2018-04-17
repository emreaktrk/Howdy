package istanbul.codify.muudy.ui.chat.decorator;

import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.helper.Decorator;

public class OutgoingChatDecorator extends Decorator<View> {

    public OutgoingChatDecorator(View object) {
        super(object);
    }

    @Override
    protected void decorate(View object) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) object.getLayoutParams();
        params.gravity = Gravity.END;

        object.setBackgroundResource(R.drawable.background_chat_outgoing);

        if (object instanceof AppCompatTextView) {
            AppCompatTextView text = (AppCompatTextView) object;
            text.setTextColor(Color.BLACK);
        }
    }
}
