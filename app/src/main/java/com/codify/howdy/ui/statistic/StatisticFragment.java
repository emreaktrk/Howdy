package com.codify.howdy.ui.statistic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.codify.howdy.R;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;
import com.codify.howdy.ui.statistic.event.StatisticEventFragment;
import com.codify.howdy.ui.statistic.map.StatisticMapFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public final class StatisticFragment extends NavigationFragment implements StatisticView {

    private StatisticPresenter mPresenter = new StatisticPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic;
    }

    @Override
    public int getSelection() {
        return Navigation.STATISTIC;
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
    public FragmentPagerItemAdapter create() {
        return new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                FragmentPagerItems
                        .with(getContext())
                        .add("Etkinlik", StatisticEventFragment.class)
                        .add("Harita", StatisticMapFragment.class)
                        .create());
    }
}
