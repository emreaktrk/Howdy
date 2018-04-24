package istanbul.codify.muudy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import istanbul.codify.muudy.R;

public class NumberView extends LinearLayoutCompat {

    private AppCompatTextView mValue;
    private AppCompatTextView mText;

    public NumberView(@NonNull Context context) {
        super(context);

        init();
    }

    public NumberView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NumberView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        inflate(getContext(), R.layout.cell_number_view, this);
        mValue = findViewById(R.id.number_view_value);
        mText = findViewById(R.id.number_view_text);
    }

    public void setValue(int value) {
        mValue.setText(value + "");
    }

    public void setValue(CharSequence value) {
        mValue.setText(value);
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }
}
