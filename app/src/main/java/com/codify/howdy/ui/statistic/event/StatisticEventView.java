package com.codify.howdy.ui.statistic.event;

import android.support.annotation.Nullable;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Activity;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;

interface StatisticEventView extends MvpView {

    void onLoaded(@Nullable List<Activity> activities, @Nullable List<Word> words);

    void onError(ApiError error);

    void onActivityClicked(Activity activity);
}
