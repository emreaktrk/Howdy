package com.codify.howdy.ui.statistic;

import com.codify.howdy.R;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;

public final class StatisticFragment extends NavigationFragment implements StatisticView {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic;
    }

    @Override
    public int getSelection() {
        return Navigation.STATISTIC;
    }
}
