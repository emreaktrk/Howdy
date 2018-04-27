package istanbul.codify.muudy.ui.statistic.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }
}
