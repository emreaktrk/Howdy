package istanbul.codify.muudy.ui.media;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.MuudyBottomSheet;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.MediaType;

public final class MediaBottomSheet extends MuudyBottomSheet implements MediaView {

    private MediaPresenter mPresenter = new MediaPresenter();
    private OnMediaClickListener.OnGalleryClickListener mGallery;
    private OnMediaClickListener.OnCameraClickListener mCamera;

    public static MediaBottomSheet newInstance() {
        return new MediaBottomSheet();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_media_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onMediaTypeSelected(MediaType type) {
        switch (type) {
            case CAMERA:
                if (mCamera != null) {
                    mCamera.onCameraClick();
                }
                break;
            case GALLERY:
                if (mGallery != null) {
                    mGallery.onGalleryClick();
                }
                break;
            default:
                throw new IllegalStateException("Not implemented");
        }

        dismiss();
    }

    public MediaBottomSheet setOnCameraClickListener(OnMediaClickListener.OnCameraClickListener listener) {
        mCamera = listener;

        return this;
    }

    public MediaBottomSheet setOnGalleryClickListener(OnMediaClickListener.OnGalleryClickListener listener) {
        mGallery = listener;

        return this;
    }

    public interface OnMediaClickListener {

        interface OnGalleryClickListener {
            void onGalleryClick();
        }

        interface OnCameraClickListener {
            void onCameraClick();
        }
    }
}
