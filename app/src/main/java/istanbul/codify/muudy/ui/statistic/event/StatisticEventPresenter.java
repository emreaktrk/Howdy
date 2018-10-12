package istanbul.codify.muudy.ui.statistic.event;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

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

    List<Activity> mActivities;
    List<ActivityStat> mStats;
    ActivityAdapter activityAdapter;
    ActivityStatsAdapter wordAdapter;
    int mPostCount = 0;
    Activity mActivity;

    @Override
    public void attachView(StatisticEventView view, View root) {
        super.attachView(view, root);
        mDisposables.add(
                RxSwipeRefreshLayout
                        .refreshes(findViewById(R.id.statistic_event_refresh))
                        .subscribe(o -> {
                            Logcat.v("Refresh clicked");

                            view.onRefresh();
                        }));
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
                                mActivities = response.data;
                                mView.onLoaded(response.data, null,0);

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats, @Nullable int postCount) {
        mPostCount = postCount;
        if (activities != null) {

                activityAdapter = new ActivityAdapter(activities, null);
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
            List<ActivityStat> statsDetail = new ArrayList<>();
            for (int i = 0; i <stats.size() ; i++) {
                if (i + 1 == stats.size() && stats.size() == 6){
                    statsDetail.addAll(stats.get(i).otherEmojis);
                }else{
                    statsDetail.add(stats.get(i));
                }
            }

                wordAdapter = new ActivityStatsAdapter(stats, statsDetail, postCount);
                mDisposables.add(
                        wordAdapter
                                .itemClicks()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(stat -> {
                                    Logcat.v("Activity stat clicked");

                                    mView.onActivityStatSelected(stat);
                                }));
                mDisposables.add(
                        wordAdapter
                                .showAllPostsClicks()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(view -> {
                                    Logcat.v("Show all posts clicked");


                                    Activity selected = activityAdapter.getSelected();
                                    if (selected != null) {
                                        mView.onShowAllPostsClicked(selected);
                                    }

                                }));

                findViewById(R.id.statistic_event_word, RecyclerView.class).setAdapter(wordAdapter);

        }
    }

    void bind(@Nullable List<Activity> activities, @Nullable List<ActivityStat> stats, @Nullable int postCount, @Nullable boolean isFromCache) {
        if (activities != null) {

                activityAdapter = new ActivityAdapter(activities, null);
                mDisposables.add(
                        activityAdapter
                                .itemClicks()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(activity -> {
                                    Logcat.v("Activity clicked");

                                    mView.onActivityClicked(activity);
                                }));
                findViewById(R.id.statistic_event_activity, RecyclerView.class).setAdapter(activityAdapter);



        }

        if (stats != null) {
            List<ActivityStat> statsDetail = new ArrayList<>();
            for (int i = 0; i < stats.size(); i++) {
                if (i + 1 == stats.size() && stats.size() == 6) {
                    statsDetail.addAll(stats.get(i).otherEmojis);
                } else {
                    statsDetail.add(stats.get(i));
                }
            }

                wordAdapter = new ActivityStatsAdapter(stats, statsDetail, postCount);
                mDisposables.add(
                        wordAdapter
                                .itemClicks()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(stat -> {
                                    Logcat.v("Activity stat clicked");

                                    mView.onActivityStatSelected(stat);
                                }));
                mDisposables.add(
                        wordAdapter
                                .showAllPostsClicks()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(view -> {
                                    Logcat.v("Show all posts clicked");


                                    Activity selected = activityAdapter.getSelected();
                                    if (selected != null) {
                                        mView.onShowAllPostsClicked(selected);
                                    }

                                }));

                findViewById(R.id.statistic_event_word, RecyclerView.class).setAdapter(wordAdapter);

        }
        if (mActivity != null) {
            bind(mActivity);
        }
        getStats(mActivity);


    }

    void bind(Activity activity) {
        mActivity = activity;
        //ActivityAdapter adapter = (ActivityAdapter) findViewById(R.id.statistic_event_activity, RecyclerView.class).getAdapter();
        activityAdapter.setSelected(activity);
        activityAdapter.notifyDataSetChanged();
    }

    @Nullable
    Activity getSelectedActivity() {
        //ActivityAdapter adapter = (ActivityAdapter) findViewById(R.id.statistic_event_activity, RecyclerView.class).getAdapter();
        return activityAdapter.getSelected();
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
                                mStats = response.data.emojiRows;
                                mView.onLoaded(null, response.data.emojiRows,response.data.postCount);
                                findViewById(R.id.statistic_event_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                findViewById(R.id.statistic_event_refresh, SwipeRefreshLayout.class).setRefreshing(false);
                                mView.onError(error);
                            }
                        }));
    }
}
