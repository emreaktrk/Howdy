package istanbul.codify.muudy.ui.statistic.event;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.ActivityStat;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface StatisticEventView extends MvpView {

    void onLoaded(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats);

    void onError(ApiError error);

    void onActivityClicked(Activity activity);

    void onActivityStatSelected(ActivityStat stat);
}
