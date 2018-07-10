package istanbul.codify.muudy.ui.photo;

import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.ui.base.BasePresenter;
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

    void bind(Uri uri) {

        findViewById(R.id.photo_image, AppCompatImageView.class).setImageURI(uri);
    }
}
