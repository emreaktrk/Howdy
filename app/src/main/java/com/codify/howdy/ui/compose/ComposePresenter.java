package com.codify.howdy.ui.compose;

import android.Manifest;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetWordsWithFilterRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Activity;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.List;

final class ComposePresenter extends BasePresenter<ComposeView> {

    private Uri mPhoto;
    private Activity mActivity;
    private final List<Word> mSelecteds = new ArrayList<>();
    private final SelectedAdapter mAdapter = new SelectedAdapter(mSelecteds);

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
                        .clicks(findViewById(R.id.compose_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_search))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Search clicked");
                            view.onSearchClicked();
                        }));

        mDisposables.add(
                mAdapter
                        .removeClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(word -> {
                            Logcat.v("Selected word remove clicked");
                            mView.onWordRemoved(word);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_picture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture clicked");
                            mView.onPictureClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_cancel))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Picture cancelled");
                            mView.onPhotoCancelClicked();
                        }));

        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.compose_selected_word_recycler, RecyclerView.class).setAdapter(mAdapter);
    }

    void getWordsWithFilter(@Nullable Activity activity) {
        setSelectedActivity(activity);
        GetWordsWithFilterRequest request = new GetWordsWithFilterRequest(mActivity == null ? 0 : mActivity.idactivities, mSelecteds);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWordsWithFilter(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsWithFilterResponse>() {
                            @Override
                            protected void success(GetWordsWithFilterResponse response) {
                                mView.onLoaded(response.data.topCategories, response.data.activites);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void getWordsWithFilter() {
        getWordsWithFilter(null);
    }

    void bind(ArrayList<Category> categories) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        mDisposables.add(
                categoryAdapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(category -> {
                            Logcat.v("Category clicked");
                            mView.onCategoryClicked(category);
                        }));
        findViewById(R.id.compose_category_recycler, RecyclerView.class).setAdapter(categoryAdapter);
    }

    void bind(ArrayList<Category> categories, ArrayList<Activity> activities) {
        bind(categories);

        ActivityAdapter activityAdapter = new ActivityAdapter(activities, mActivity);
        mDisposables.add(
                activityAdapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(activity -> {
                            Logcat.v("Activity clicked");
                            mView.onActivityClicked(activity);
                        }));
        findViewById(R.id.compose_activity_recycler, RecyclerView.class).setAdapter(activityAdapter);
    }

    void addSelectedWord(Word word) {
        mSelecteds.add(word);
        mAdapter.notifyDataSetChanged(mSelecteds);
    }

    void removeSelectedWord(Word word) {
        mSelecteds.remove(word);
        mAdapter.notifyDataSetChanged(mSelecteds);
    }

    List<Word> getSelectedWords() {
        return mSelecteds;
    }

    void setSelectedActivity(@Nullable Activity activity) {
        mActivity = activity;
    }

    void removeSelectedActivity() {
        mActivity = null;
    }

    void selectPhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<List<Uri>>>) granted -> granted ? RxGallery.gallery(activity, false).toObservable() : Observable.empty())
                        .subscribe(uris -> {
                            Uri uri = uris.get(0);
                            Logcat.v("Selected uri for photo is " + uri.toString());
                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void capturePhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<Uri>>) granted -> granted ? RxGallery.photoCapture(activity).toObservable() : Observable.empty())
                        .subscribe(uri -> {
                            Logcat.v("Selected uri for photo is " + uri.toString());
                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void bind(@Nullable Uri photo) {
        mPhoto = photo;

        Picasso
                .with(getContext())
                .load(mPhoto)
                .into(findViewById(R.id.compose_picture, AppCompatImageButton.class));

        findViewById(R.id.compose_cancel).setVisibility(View.VISIBLE);
    }

    void cancelPhoto() {
        mPhoto = null;

        findViewById(R.id.compose_picture, AppCompatImageButton.class).setImageBitmap(null);
        findViewById(R.id.compose_cancel).setVisibility(View.GONE);
    }
}
