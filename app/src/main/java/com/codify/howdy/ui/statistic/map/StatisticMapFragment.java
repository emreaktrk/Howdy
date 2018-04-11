package com.codify.howdy.ui.statistic.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;

public final class StatisticMapFragment extends HowdyFragment implements StatisticMapView {

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
