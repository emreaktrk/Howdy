package istanbul.codify.monju.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;
import com.blankj.utilcode.util.SizeUtils;

public class MentionButton extends ToggleButton {

    public MentionButton(Context context) {
        super(context);

        init();
    }

    public MentionButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MentionButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        setText(checked ? "Kaldır" : "Ekle");
    }
}
