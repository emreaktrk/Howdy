package istanbul.codify.monju.ui.compose.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;

/**
 * Created by egesert on 10.07.2018.
 */

public class ChooseSeasonDialogPresenter extends BasePresenter<ChooseSeasonDialogView> {


    String [] seasons = {"Sezon","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    String [] episodes= {"Bölüm","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40"};


    @Override
    public void attachView(ChooseSeasonDialogView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.choose_season_dialog_share))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");
                            view.onOkClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.choose_season_dialog_layer))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

    }

    void bind(){
        ArrayAdapter<String> seasonsAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,R.id.spinner_item_text, seasons);
        findViewById(R.id.choose_season_dialog_season_spinner, AppCompatSpinner.class).setAdapter(seasonsAdapter);

        ArrayAdapter<String> episodesAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,R.id.spinner_item_text, episodes);
        findViewById(R.id.choose_season_dialog_episode_spinner, AppCompatSpinner.class).setAdapter(episodesAdapter );
    }

    void addBlurredBackground(Bitmap bitmap){
        findViewById(R.id.choose_season_dialog_background, AppCompatImageView.class).setBackgroundDrawable(new BitmapDrawable(getContext().getResources(),bitmap));
    }

    String getSelectedSeason() {
        AppCompatSpinner seasonSpinner = findViewById(R.id.choose_season_dialog_season_spinner, AppCompatSpinner.class);
        Object selectedItem = seasonSpinner .getSelectedItem();
        int position = seasonSpinner .getSelectedItemPosition();
        if(selectedItem instanceof String){
            if(((String)selectedItem).equals(seasons[position]) && position != 0){
                return seasons[position];
            }else{
                return null;
            }
        }
        return null;

    }

    String getSelectedEpisode() {
        AppCompatSpinner episodeSpinner = findViewById(R.id.choose_season_dialog_episode_spinner, AppCompatSpinner.class);
        Object selectedItem = episodeSpinner.getSelectedItem();
        int position = episodeSpinner.getSelectedItemPosition();

        if(selectedItem instanceof String){
            if(((String)selectedItem).equals(episodes[position]) && position != 0){
                return episodes[position];
            }else{
                return null;
            }
        }
        return null;

    }

}
