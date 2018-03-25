package com.codify.howdy.ui.profile;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;

final class ProfilePresenter extends BasePresenter<ProfileView> {

    @Override
    public void attachView(ProfileView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Edit clicked");
                            view.onEditClicked();
                        }));
    }

    void bind(User user) {
        findViewById(R.id.profile_username, AppCompatTextView.class).setText(user.username);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath)
                .into(findViewById(R.id.profile_picture, CircleImageView.class));
    }
}
