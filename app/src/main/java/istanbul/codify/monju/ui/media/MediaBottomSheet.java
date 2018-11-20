package istanbul.codify.monju.ui.media;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.monju.MuudyBottomSheet;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.MediaType;
import istanbul.codify.monju.model.User;

public final class MediaBottomSheet extends MuudyBottomSheet implements MediaView {

    private User user;
    private MediaPresenter mPresenter = new MediaPresenter();

    private OnMediaClickListener.OnGalleryClickListener mGallery;
    private OnMediaClickListener.OnCameraClickListener mCamera;
    private OnMediaClickListener.OnVideoClickListener mVideo;
    private OnMediaClickListener.OnMakeProfileImageListener mMakeProfileImage;

    @SuppressLint("ValidFragment")
    public MediaBottomSheet(User user) {
        super();
        this.user = user;
    }

    public MediaBottomSheet() {
        super();
    }

    public static MediaBottomSheet newInstance() {
        return new MediaBottomSheet();
    }

    public static MediaBottomSheet newInstance(User user) {

        return new MediaBottomSheet(user);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_media_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        if (mGallery == null) {
            mPresenter.hideGallery();
        }

        if (mCamera == null) {
            mPresenter.hideCamera();
        }

        if (mVideo == null) {
            mPresenter.hideVideo();
        }

        if(mMakeProfileImage == null){
            mPresenter.hideMakeProfileImage();
        }
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
            case VIDEO:
                if (mVideo != null) {
                    mVideo.onVideoClick();
                }
                break;


            default:
                throw new IllegalStateException("Not implemented");
        }

        dismiss();
    }

    @Override
    public void onMakeProfileImage() {
        mMakeProfileImage.onMakeProfileImageClick(user);
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

    public MediaBottomSheet setOnVideoClickListener(OnMediaClickListener.OnVideoClickListener listener) {
        mVideo = listener;

        return this;
    }

    public MediaBottomSheet setOnMakeProfileImageListener(OnMediaClickListener.OnMakeProfileImageListener listener) {
        mMakeProfileImage = listener;

        return this;
    }


    public interface OnMediaClickListener {

        interface OnGalleryClickListener {
            void onGalleryClick();
        }

        interface OnCameraClickListener {
            void onCameraClick();
        }

        interface OnVideoClickListener {
            void onVideoClick();
        }

        interface OnMakeProfileImageListener {
            void onMakeProfileImageClick(User user);
        }
    }
}
