package com.codify.howdy.ui.statistic.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;

public final class StatisticEventFragment extends HowdyFragment implements StatisticEventView{

    private StatisticEventPresenter mPresenter = new StatisticEventPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic_event;
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
