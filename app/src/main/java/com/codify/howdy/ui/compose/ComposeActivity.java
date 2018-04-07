package com.codify.howdy.ui.compose;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.analytics.Analytics;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.*;
import com.codify.howdy.ui.places.PlacesActivity;
import com.codify.howdy.ui.word.WordActivity;

import java.util.ArrayList;


public final class ComposeActivity extends HowdyActivity implements ComposeView {

    private final ComposePresenter mPresenter = new ComposePresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ComposeActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_compose;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
        mPresenter.getWordsWithFilter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked() {
        Analytics
                .getInstance()
                .custom(Analytics.Events.COMPOSE);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onSearchClicked() {
        WordActivity.start(mPresenter.getSelecteds(), null, ResultTo.ACTIVITY);
    }

    @Override
    public void onLoaded(ArrayList<Category> categories, ArrayList<Activity> activities) {
        mPresenter.bind(categories, activities);
    }

    @Override
    public void onLoaded(ArrayList<Category> filtered) {
        mPresenter.bind(filtered);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onCategoryClicked(Category category) {
        if (category.isLocation()) {
            PlacesActivity.start(ResultTo.ACTIVITY);
        } else {
            WordActivity.start(category, ResultTo.ACTIVITY);
        }
    }

    @Override
    public void onActivityClicked(Activity activity) {
        mPresenter.addSelected(activity);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onSelectedRemoved(Selectable selected) {
        mPresenter.removeSelected(selected);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onPictureClicked() {
        mPresenter.selectPhoto(this);
    }

    @Override
    public void onPhotoSelected(Uri uri) {
        mPresenter.bind(uri);
    }

    @Override
    public void onPhotoCancelClicked() {
        mPresenter.cancelPhoto();
    }

    @SuppressWarnings({"unchecked", "UnnecessaryReturnStatement"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Word word = resolveResult(requestCode, resultCode, data, Word.class, WordActivity.REQUEST_CODE);
        if (word != null) {
            mPresenter.addSelected(word);
            mPresenter.getWordsWithFilter();
            return;
        }

        ArrayList<User> users = resolveResult(requestCode, resultCode, data, ArrayList.class, WordActivity.REQUEST_CODE);
        if (users != null) {
            mPresenter.addSelected(users);
            mPresenter.getWordsWithFilter();
            return;
        }

        Place place = resolveResult(requestCode, resultCode, data, Place.class, PlacesActivity.REQUEST_CODE);
        if (place != null) {
            mPresenter.addSelected(place);
            mPresenter.getWordsWithFilter();
            return;
        }
    }
}
