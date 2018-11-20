package istanbul.codify.monju.ui.statistic.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Activity;
import istanbul.codify.monju.model.ActivityStat;
import istanbul.codify.monju.ui.posts.PostsActivity;

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
        if (mPresenter.mActivities != null){
            mPresenter.bind(mPresenter.mActivities,mPresenter.mStats,mPresenter.mPostCount,true);
        }else{
            mPresenter.getActivities();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats, @Nullable int postCount) {
        mPresenter.bind(activities, stats, postCount);
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
    public void onShowAllPostsClicked(Activity activity) {
        PostsActivity.start(activity);
    }

    @Override
    public void onActivityStatSelected(ActivityStat stat) {
        Activity activity = mPresenter.getSelectedActivity();
        if (activity != null) {
            PostsActivity.start(activity, stat.post_emoji_word);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.bind(mPresenter.mActivity);
        mPresenter.getStats(mPresenter.mActivity);
    }
}
