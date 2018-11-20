package istanbul.codify.monju.ui.compose.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import istanbul.codify.monju.MuudyDialog;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.event.SeasonSelectionEvent;

/**
 * Created by egesert on 10.07.2018.
 */

public class ChooseSeasonDialog extends MuudyDialog implements ChooseSeasonDialogView {

    ChooseSeasonDialogPresenter mPresenter = new ChooseSeasonDialogPresenter();

    Boolean isShared = false;
    public static ChooseSeasonDialog newInstance(Bitmap bitmap) {
        Bundle args = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        args.putByteArray("bitmapImage",byteArray);
        ChooseSeasonDialog fragment = new ChooseSeasonDialog();
        fragment.setArguments(args);
        return fragment;
    }

/*    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }*/

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_choose_season_dialog;
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
    public void onDismiss(DialogInterface dialog) {
        SeasonSelectionEvent event = new SeasonSelectionEvent();

        if(!isShared) {
            EventBus
                    .getDefault()
                    .post(event);
        }
        super.onDismiss(dialog);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        byte[] byteArray = getArguments().getByteArray("bitmapImage");
        if(byteArray != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (bmp != null) {
                mPresenter.addBlurredBackground(bmp);
            }
        }
        mPresenter.bind();

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onOkClicked() {
        String selectedSeason = mPresenter.getSelectedSeason();
        String selectedEpisode = mPresenter.getSelectedEpisode();

        if(selectedSeason != null && selectedEpisode != null){
            SeasonSelectionEvent event = new SeasonSelectionEvent();
            event.selectedSeasonAndEpisode = "S" + selectedSeason + "-B" + selectedEpisode;

            isShared = true;
                    EventBus
                    .getDefault()
                    .post(event);

            dismiss();
        }else if(selectedSeason != null && selectedEpisode == null){
            Toast.makeText(getContext(), "Bölüm seçmelisiniz", Toast.LENGTH_SHORT).show();
        }else if(selectedSeason == null && selectedEpisode != null){
            Toast.makeText(getContext(), "Sezon seçmelisiniz", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Sezon ve bölüm seçmelisiniz", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCloseClicked() {
        isShared = false;
        dismiss();
    }
}
