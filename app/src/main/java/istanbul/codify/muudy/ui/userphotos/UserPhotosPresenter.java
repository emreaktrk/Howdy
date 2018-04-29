package istanbul.codify.muudy.ui.userphotos;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class UserPhotosPresenter extends BasePresenter<UserPhotosView> {

    @Override
    public void attachView(UserPhotosView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_photos_back))
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));
    }

    void bind(User user) {
        ViewPager pager = findViewById(R.id.user_photos_pager, ViewPager.class);
        pager.setAdapter(mView.create(user));
    }
}
