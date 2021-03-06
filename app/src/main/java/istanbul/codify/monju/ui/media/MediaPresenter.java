package istanbul.codify.monju.ui.media;

import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.MediaType;
import istanbul.codify.monju.ui.base.BasePresenter;

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

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.media_dialog_make_profile_image))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("MakeProfileImage clicked");

                            view.onMakeProfileImage();
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

    void hideMakeProfileImage(){
        findViewById(R.id.media_dialog_make_profile_image).setVisibility(View.GONE);
    }
}
