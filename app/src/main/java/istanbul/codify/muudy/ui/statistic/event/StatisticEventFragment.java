package istanbul.codify.muudy.ui.statistic.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.ActivityStat;

import java.util.List;

public final class StatisticEventFragment extends MuudyFragment implements StatisticEventView {

    private StatisticEventPresenter mPresenter = new StatisticEventPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic_event;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
        mPresenter.getActivities();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats) {
        mPresenter.bind(activities, stats);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onActivityClicked(Activity activity) {
        mPresenter.bind(activity);
        mPresenter.getStats(activity);
    }

    @Override
    public void onActivityStatSelected(ActivityStat stat) {
        mPresenter.bind(stat);
    }
}
