package istanbul.codify.muudy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;

public class AvatarView extends LinearLayoutCompat {

    private CircleImageView mImage;
    private AppCompatTextView mName;

    public AvatarView(@NonNull Context context) {
        super(context);

        init();
    }

    public AvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AvatarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        inflate(getContext(), R.layout.cell_avatar_view, this);

        mImage = findViewById(R.id.avatar_view_image);
        mName = findViewById(R.id.avatar_view_name);

    }

    public void setName(CharSequence name) {
        mName.setText(name);
    }

    public void setImage(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    public void setUser(@Nullable User user) {
        if (user == null) {
            mName.setText(null);
            mImage.setImageBitmap(null);
        } else {
            mName.setText(user.username);
            Picasso
                    .with(getContext())
                    .load(BuildConfig.URL + user.imgpath1)
                    .placeholder(R.drawable.ic_avatar)
                    .into(mImage);
        }
    }
}
