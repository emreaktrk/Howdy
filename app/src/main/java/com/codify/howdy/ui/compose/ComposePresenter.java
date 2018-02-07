package com.codify.howdy.ui.compose;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetWordsResponse;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterRequest;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Category;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class ComposePresenter extends BasePresenter<ComposeView> {

    @Override
    public void attachView(ComposeView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_search))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Search clicked");
                            view.onSearchClicked();
                        }));

        findViewById(R.id.compose_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(root.getContext()));
    }

    void getWords() {
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWords()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsResponse>() {
                            @Override
                            protected void success(GetWordsResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void getWords(Category... selecteds) {
        getWords(0, Arrays.asList(selecteds));
    }

    void getWords(long activityId, Category... selecteds) {
        getWords(activityId, Arrays.asList(selecteds));
    }

    void getWords(List<Category> selecteds) {
        getWords(0, selecteds);
    }

    void getWords(long activityId, List<Category> selecteds) {
        GetWordsWithFilterRequest request = new GetWordsWithFilterRequest(activityId, selecteds);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWordsWithFilter(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsWithFilterResponse>() {
                            @Override
                            protected void success(GetWordsWithFilterResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    public void bind(ArrayList<Category> list) {
        CategoryAdapter adapter = new CategoryAdapter(list);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(emotion -> {
                            Logcat.v("Category clicked");
                            mView.onCategoryClicked(emotion);
                        }));
        findViewById(R.id.compose_recycler, RecyclerView.class).setAdapter(adapter);
    }
}
