package istanbul.codify.muudy.ui.userphotos;

import android.support.v4.view.ViewPager;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class UserPhotosPresenter extends BasePresenter<UserPhotosView> {

    void bind(User user) {
        ViewPager pager = findViewById(R.id.user_photos_pager, ViewPager.class);
        pager.setAdapter(mView.create());
    }
}
