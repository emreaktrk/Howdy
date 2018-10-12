package istanbul.codify.muudy.ui.statistic.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.VisibleRegion;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.EmojiLocation;
import istanbul.codify.muudy.ui.MarkerCustomPopup.MarkerCustomPopupFragmentDialog;

import java.util.List;

public final class StatisticMapFragment extends MuudyFragment implements StatisticMapView {

    private StatisticMapPresenter mPresenter = new StatisticMapPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
        mPresenter
                .map().isAttachedToWindow();//sadas
        mPresenter
                .map()
                .onCreate(savedInstanceState);

    }

    @Override
    public void onReady(GoogleMap map) {
        mPresenter.bind(map);
    }

    @Override
    public void onArea(GoogleMap map, VisibleRegion region) {
        mPresenter.getEmojis(map, region);
    }

    @Override
    public void onLoaded(GoogleMap map, List<EmojiLocation> locations) {
        mPresenter.icons(map, locations);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onMarkerClicked(EmojiLocation emojiLocation) {
        new MarkerCustomPopupFragmentDialog(emojiLocation)
                .show(getChildFragmentManager(), null);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter
                .map()
                .onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter
                .map()
                .onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter
                .map()
                .onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter
                .map()
                .onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter
                .map()
                .onDestroy();

        mPresenter.detachView();
    }
}
