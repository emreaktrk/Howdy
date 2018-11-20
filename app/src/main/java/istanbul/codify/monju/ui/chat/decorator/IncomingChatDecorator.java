package istanbul.codify.monju.ui.chat.decorator;

import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import istanbul.codify.monju.R;
import istanbul.codify.monju.helper.Decorator;

public class IncomingChatDecorator extends Decorator<View> {


    public IncomingChatDecorator(View object) {
        super(object);
    }

    public IncomingChatDecorator(View object, View object2, View object3) {
        super(object, object2, object3);
    }

    @Override
    protected void decorate(View object) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) object.getLayoutParams();
        params.gravity = Gravity.START;

        object.setBackgroundResource(R.drawable.background_chat_incoming);
    }

    @Override
    protected void decorate(View object, View object2, View object3) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) object.getLayoutParams();
        params.gravity = Gravity.START;

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) object2.getLayoutParams();
        params2.gravity = Gravity.START;

        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) object3.getLayoutParams();
        params3.gravity = Gravity.START;

        object.setBackgroundResource(R.drawable.background_chat_incoming);

        if (object3 instanceof AppCompatTextView) {
            AppCompatTextView text = (AppCompatTextView) object3;
            text.setTextColor(Color.WHITE);
        }

        if (object2 instanceof AppCompatTextView) {
            AppCompatTextView text = (AppCompatTextView) object2;
            text.setTextColor(object.getContext().getResources().getColor(R.color.message_date));
        }
    }

}
