package com.codify.howdy.ui.statistic.event;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetActivityStatsRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetActivitiesResponse;
import com.codify.howdy.api.pojo.response.GetActivityStatsResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Activity;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.BasePresenter;
import com.codify.howdy.ui.compose.ActivityAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

final class StatisticEventPresenter extends BasePresenter<StatisticEventView> {

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

    void bind(@Nullable List<Activity> activities, @Nullable List<Word> words) {
        if (activities != null) {
            ActivityAdapter adapter = new ActivityAdapter(activities, null);
            mDisposables.add(
                    adapter
                            .itemClicks()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(activity -> {
                                Logcat.v("Activity clicked");

                                mView.onActivityClicked(activity);
                            }));
            findViewById(R.id.statistic_event_recycler, RecyclerView.class).setAdapter(adapter);
        }
    }

    void bind(Activity activity) {
        ActivityAdapter adapter = (ActivityAdapter) findViewById(R.id.statistic_event_recycler, RecyclerView.class).getAdapter();
        adapter.setSelected(activity);
        adapter.notifyDataSetChanged();
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
