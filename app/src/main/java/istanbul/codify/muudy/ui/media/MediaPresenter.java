package istanbul.codify.muudy.ui.media;

import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.MediaType;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class MediaPresenter extends BasePresenter<MediaView> {

    @Override
    public void attachView(MediaView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.media_dialog_gallery))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Gallery clicked");

                            view.onMediaTypeSelected(MediaType.GALLERY);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.media_dialog_capture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Capture clicked");

                            view.onMediaTypeSelected(MediaType.CAMERA);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.media_dialog_video))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Video clicked");

                            view.onMediaTypeSelected(MediaType.VIDEO);
                        }));
    }

    void hideGallery() {
        findViewById(R.id.media_dialog_gallery).setVisibility(View.GONE);
    }

    void hideCamera() {
        findViewById(R.id.media_dialog_capture).setVisibility(View.GONE);
    }

    void hideVideo() {
        findViewById(R.id.media_dialog_video).setVisibility(View.GONE);
    }
}
