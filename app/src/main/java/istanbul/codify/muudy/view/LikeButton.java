package istanbul.codify.muudy.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import com.blankj.utilcode.util.StringUtils;
import istanbul.codify.muudy.R;

public class LikeButton extends LinearLayoutCompat implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private AppCompatCheckBox mIcon;
    private AppCompatTextView mText;

    private OnLikeClickListener mListener;

    public LikeButton(Context context) {
        super(context);

        init();
    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        inflate(getContext(), R.layout.cell_like_view, this);

        mIcon = findViewById(R.id.like_view_icon);
        mText = findViewById(R.id.like_view_text);

        mIcon.setOnCheckedChangeListener(this);
        mText.setOnClickListener(this);
    }

    public void setLike(boolean isLiked) {
        mIcon.setOnCheckedChangeListener(null);
        mIcon.setChecked(isLiked);
        mIcon.setOnCheckedChangeListener(this);
    }

    public void setOnLikeClickListener(OnLikeClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        String text = mText.getText().toString().replace(" ", "");
        if (!StringUtils.isEmpty(text)) {
            Integer count = Integer.valueOf(text);
            setText(isChecked ? (count + 1) + "" : (count - 1) + "");
        }

        if (mListener != null) {
            mListener.onLike(isChecked);
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            String text = mText.getText().toString().replace(" ", "");
            if (!StringUtils.isEmpty(text)) {
                Integer count = Integer.valueOf(text);
                if (count > 0) {
                    mListener.onLikeTextClicked();
                }
            }
        }
    }

    public void setText(CharSequence value) {
        mText.setText(value);
    }

    public interface OnLikeClickListener {
        void onLike(boolean isLiked);

        void onLikeTextClicked();
    }
}
