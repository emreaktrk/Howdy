package istanbul.codify.monju.ui.statistic.event;

import android.support.annotation.Nullable;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Activity;
import istanbul.codify.monju.model.ActivityStat;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;

interface StatisticEventView extends MvpView {

    void onLoaded(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats,@Nullable int postCount);

    void onError(ApiError error);

    void onActivityClicked(Activity activity);

    void onShowAllPostsClicked(Activity activity);

    void onActivityStatSelected(ActivityStat stat);

    void onRefresh();

}
