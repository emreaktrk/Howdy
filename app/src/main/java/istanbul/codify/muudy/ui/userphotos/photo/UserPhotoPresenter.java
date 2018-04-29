package istanbul.codify.muudy.ui.userphotos.photo;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.ui.base.BasePresenter;

class UserPhotoPresenter extends BasePresenter<UserPhotoView> {

    void bind(String path) {
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + path)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.user_photo_image, CircleImageView.class));
    }
}
