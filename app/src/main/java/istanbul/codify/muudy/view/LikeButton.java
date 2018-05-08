package istanbul.codify.muudy.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;

public class LikeButton extends AppCompatCheckBox implements CompoundButton.OnCheckedChangeListener {

    private OnLikeClickListener mListener;

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
        setOnCheckedChangeListener(this);
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

    public void setLike(boolean isLiked) {
        setOnCheckedChangeListener(null);
        setChecked(isLiked);
        setOnCheckedChangeListener(this);
    }

    public void setOnLikeClickListener(OnLikeClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        String text = getText().toString().replace(" ", "");
        if (!StringUtils.isEmpty(text)) {
            Integer count = Integer.valueOf(text);
            setText(isChecked ? (count + 1) + "" : (count - 1) + "");
        }

        if (mListener != null) {
            mListener.onLike(isChecked);
        }
    }

    public interface OnLikeClickListener {
        void onLike(boolean isLiked);
    }
}
