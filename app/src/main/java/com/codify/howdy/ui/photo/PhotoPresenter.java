package com.codify.howdy.ui.photo;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;

final class PhotoPresenter extends BasePresenter<PhotoView> {

    @Override
    public void attachView(PhotoView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.photo_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));
    }

    void bind(String url) {
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + url)
                .into(findViewById(R.id.photo_image, AppCompatImageView.class));
    }
}
