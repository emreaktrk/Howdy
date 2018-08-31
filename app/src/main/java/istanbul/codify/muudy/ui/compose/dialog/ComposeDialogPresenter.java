package istanbul.codify.muudy.ui.compose.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.PostVisibility;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class ComposeDialogPresenter extends BasePresenter<ComposeDialogView> {

    public Boolean isFacebookSelected = false;
    public Boolean isTwitterSelected = false;

    @Override
    public void attachView(ComposeDialogView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_dialog_share))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");

                            view.onShareClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_dialog_facebook))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");

                            view.onFacebookClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_dialog_twitter))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");

                            view.onTwitterClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_dialog_layer))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));
    }

    void bind(String sentence) {
        findViewById(R.id.compose_dialog_sentence, AppCompatTextView.class).setText(sentence);

        ArrayAdapter<PostVisibility> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,R.id.spinner_item_text, PostVisibility.values());
        findViewById(R.id.compose_dialog_spinner, AppCompatSpinner.class).setAdapter(adapter);
    }

    PostVisibility getSelectedVisibility() {
        Object visibility = findViewById(R.id.compose_dialog_spinner, AppCompatSpinner.class).getSelectedItem();
        if (visibility instanceof PostVisibility) {
            return (PostVisibility) visibility;
        }

        return PostVisibility.ALL;
    }

    void addBlurredBackground(Bitmap bitmap){
        findViewById(R.id.compose_dialog_background, AppCompatImageView.class).setBackgroundDrawable(new BitmapDrawable(getContext().getResources(),bitmap));

    }

    void setSelectedImage(Bitmap bitmap){
        findViewById(R.id.compose_dialog_selected_image, AppCompatImageView.class).setBackgroundDrawable(new BitmapDrawable(getContext().getResources(),bitmap));
    }
    void changeFacebookImage(){
        isFacebookSelected = !isFacebookSelected;
        if (isFacebookSelected){
            findViewById(R.id.compose_dialog_facebook, AppCompatImageView.class).setImageDrawable(getContext().getResources().getDrawable(R.drawable.facebook_selected));
        }else{
            findViewById(R.id.compose_dialog_facebook, AppCompatImageView.class).setImageDrawable(getContext().getResources().getDrawable(R.drawable.facebook_unselected));
        }
    }

    void changeTwitterImage(){
        isTwitterSelected = !isTwitterSelected;
        if (isTwitterSelected ){
            findViewById(R.id.compose_dialog_twitter, AppCompatImageView.class).setImageDrawable(getContext().getResources().getDrawable(R.drawable.twitter_selected));
        }else{
            findViewById(R.id.compose_dialog_twitter, AppCompatImageView.class).setImageDrawable(getContext().getResources().getDrawable(R.drawable.twitter_unselected));
        }
    }
}
