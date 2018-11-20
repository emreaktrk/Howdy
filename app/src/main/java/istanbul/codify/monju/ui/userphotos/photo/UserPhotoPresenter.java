package istanbul.codify.monju.ui.userphotos.photo;

import android.support.v7.widget.AppCompatImageView;
import com.squareup.picasso.Picasso;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.ui.base.BasePresenter;

class UserPhotoPresenter extends BasePresenter<UserPhotoView> {

    void bind(String path) {
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + path)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.user_photo_image, AppCompatImageView.class));
    }
}
