package istanbul.codify.muudy.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import istanbul.codify.muudy.R;

public class StarView extends LinearLayoutCompat {

    private AppCompatImageView mImage;
    private AppCompatTextView mPoint;

    public StarView(@NonNull Context context) {
        super(context);

        init();
    }

    public StarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public StarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        inflate(getContext(), R.layout.cell_star_view, this);
        mPoint = findViewById(R.id.star_view_point);
        mImage = findViewById(R.id.star_view_image);
    }

    public void setPoint(@FloatRange(from = 0f, to = 5f) float point) {
        mPoint.setText(point + "");

        paintImage(point);
    }

    private void paintImage(float point) {
        int color = ColorUtils.blendARGB(ContextCompat.getColor(getContext(), R.color.star_red), ContextCompat.getColor(getContext(), R.color.star_green), point / 5f);
        mImage.setImageTintList(ColorStateList.valueOf(color));
    }

}
