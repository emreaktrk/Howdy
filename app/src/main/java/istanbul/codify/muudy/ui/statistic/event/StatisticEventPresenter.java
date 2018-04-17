package istanbul.codify.muudy.ui.statistic.event;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetActivityStatsRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetActivitiesResponse;
import istanbul.codify.muudy.api.pojo.response.GetActivityStatsResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.BasePresenter;
import istanbul.codify.muudy.ui.compose.ActivityAdapter;
import istanbul.codify.muudy.ui.word.WordAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;

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

    void bind(@Nullable List<Activity> activities, @Nullable List<Word> words) {
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
        }

        if (words != null) {
            WordAdapter wordAdapter = new WordAdapter(words);
            mDisposables.add(
                    wordAdapter
                            .itemClicks()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(word -> {
                                Logcat.v("Word clicked");
                                mView.onWordSelected(word);
                            }));

            findViewById(R.id.statistic_event_word, RecyclerView.class).setAdapter(wordAdapter);
        }
    }

    void bind(Activity activity) {
        ActivityAdapter adapter = (ActivityAdapter) findViewById(R.id.statistic_event_activity, RecyclerView.class).getAdapter();
        adapter.setSelected(activity);
        adapter.notifyDataSetChanged();
    }

    void bind(Word word) {
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