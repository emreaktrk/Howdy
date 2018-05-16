package istanbul.codify.muudy.ui.statistic.event;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetActivityStatsRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetActivitiesResponse;
import istanbul.codify.muudy.api.pojo.response.GetActivityStatsResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.ActivityStat;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.compose.ActivityAdapter;

import java.util.ArrayList;
import java.util.List;

final class StatisticEventPresenter extends BasePresenter<StatisticEventView> {

    @Override
    public void attachView(StatisticEventView view, View root) {
        super.attachView(view, root);

        findViewById(R.id.statistic_event_word, RecyclerView.class).setLayoutManager(new LinearLayoutManager(mRoot.getContext()));
    }

    void getActivities() {
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getActivities()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetActivitiesResponse>() {
                            @Override
                            protected void success(GetActivitiesResponse response) {
                                mView.onLoaded(response.data, null);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats) {
        if (activities != null) {
            ActivityAdapter activityAdapter = new ActivityAdapter(activities, null);
            mDisposables.add(
                    activityAdapter
                            .itemClicks()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(activity -> {
                                Logcat.v("Activity clicked");

                                mView.onActivityClicked(activity);
                            }));
            findViewById(R.id.statistic_event_activity, RecyclerView.class).setAdapter(activityAdapter);

            mView.onActivityClicked(activities.get(0));
        }

        if (stats != null) {
            ActivityStatsAdapter wordAdapter = new ActivityStatsAdapter(stats);
            mDisposables.add(
                    wordAdapter
                            .itemClicks()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(stat -> {
                                Logcat.v("Activity stat clicked");
                                mView.onActivityStatSelected(stat);
                            }));

            findViewById(R.id.statistic_event_word, RecyclerView.class).setAdapter(wordAdapter);

       /*     List<PieEntry> entries = new ArrayList<>();
            for (ActivityStat stat : stats) {
                entries.add(new PieEntry(Float.valueOf(stat.percent + ""), stat.post_emoji_word));
            }

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.JOYFUL_COLORS);
            set.setValueTextSize(0f);

            PieData data = new PieData(set);

            Description description = new Description();
            description.setText("");
            findViewById(R.id.statistic_event_chart, PieChart.class).setDescription(description);

            findViewById(R.id.statistic_event_chart, PieChart.class).getLegend().setEnabled(false);
            findViewById(R.id.statistic_event_chart, PieChart.class).setUsePercentValues(false);

            findViewById(R.id.statistic_event_chart, PieChart.class).setData(data);
            findViewById(R.id.statistic_event_chart, PieChart.class).invalidate();*/
        }
    }

    void bind(Activity activity) {
        ActivityAdapter adapter = (ActivityAdapter) findViewById(R.id.statistic_event_activity, RecyclerView.class).getAdapter();
        adapter.setSelected(activity);
        adapter.notifyDataSetChanged();
    }

    void bind(ActivityStat stat) {
        // TODO Bind pie chart
    }

    void getStats(Activity activity) {
        GetActivityStatsRequest request = new GetActivityStatsRequest(activity.idactivities);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getActivityStats(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetActivityStatsResponse>() {
                            @Override
                            protected void success(GetActivityStatsResponse response) {
                                mView.onLoaded(null, response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
