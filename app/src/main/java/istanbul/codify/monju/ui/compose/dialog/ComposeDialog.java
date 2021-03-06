package istanbul.codify.monju.ui.compose.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import istanbul.codify.monju.MuudyDialog;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.event.ShareEvent;
import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

public final class ComposeDialog extends MuudyDialog implements ComposeDialogView {

    private ComposeDialogPresenter mPresenter = new ComposeDialogPresenter();

    public static ComposeDialog newInstance(String sentence, Bitmap bitmap, @Nullable Bitmap selectedImage, @Nullable String emoji) {
        Bundle args = new Bundle();
        args.putSerializable(String.class.getSimpleName(), sentence);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        args.putByteArray("bitmapImage",byteArray);

        if (selectedImage != null) {
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream2);
            byte[] byteArray2 = stream2.toByteArray();
            args.putByteArray("selectedBitmapImage", byteArray2);
        }else{
            if (emoji != null){
                args.putSerializable("emoji", emoji);
            }
        }
        ComposeDialog fragment = new ComposeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_compose_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        String sentence = getSerializable(String.class);
        String emoji = getArguments().getString("emoji");
        mPresenter.bind(sentence);

        byte[] byteArray = getArguments().getByteArray("bitmapImage");
        if(byteArray != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (bmp != null) {
                mPresenter.addBlurredBackground(bmp);
            }
        }

        byte[] byteArray2 = getArguments().getByteArray("selectedBitmapImage");
        if(byteArray2 != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
            if (bmp != null) {
                mPresenter.setSelectedImage(bmp);
            }
        }else{
            if (emoji != null && emoji != ""){
                mPresenter.setEmoji(emoji);
            }else{
                mPresenter.hideSelectedImageImageView();
            }

        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // the content
        final ConstraintLayout root = new ConstraintLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onShareClicked() {
        ShareEvent event = new ShareEvent();
        event.sentence = getSerializable(String.class);
        event.visibility = mPresenter.getSelectedVisibility();
        event.shareFacebook = mPresenter.isFacebookSelected;
        event.shareTwitter = mPresenter.isTwitterSelected;

        EventBus
                .getDefault()
                .post(event);

        dismiss();
    }

    @Override
    public void onFacebookClicked() {
        mPresenter.changeFacebookImage();
    }

    @Override
    public void onTwitterClicked() {
        mPresenter.changeTwitterImage();
    }

    @Override
    public void onCloseClicked() {
        dismiss();
    }


}
