package istanbul.codify.muudy.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Place;
import istanbul.codify.muudy.model.ResultTo;

import java.util.ArrayList;

public final class PlacesActivity extends MuudyActivity implements PlacesView {

    public static final int REQUEST_CODE = 238;

    private PlacesPresenter mPresenter = new PlacesPresenter();

    String emoji;

    public static void start(@ResultTo int to,String emoji) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();
        Intent starter = new Intent(activity, PlacesActivity.class);
        starter.putExtra(emoji.getClass().getSimpleName(),emoji);
        switch (to) {
            case ResultTo.ACTIVITY:
                activity.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(activity.getSupportFragmentManager());
                fragment.startActivityForResult(starter, REQUEST_CODE);
                return;
            default:
                throw new IllegalArgumentException("Unknown ResultTo value");
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_places;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        emoji = getSerializable(String.class);
        mPresenter.getPlaces(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(ArrayList<Place> places) {
        mPresenter.bind(places);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onPlaceSearched(String query) {
        mPresenter.search(query);
    }

    @Override
    public void onPlaceClicked(Place place) {
        place.emoji = emoji;
        setResult(RESULT_OK, place);
        onBackPressed();
    }

    @Override
    public void onCloseClicked() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }
}
