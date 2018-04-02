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
import com.codify.howdy.model.Activity;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.ResultTo;
import com.codify.howdy.model.Word;
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
        WordActivity.start(mPresenter.getSelectedWords(), null, ResultTo.ACTIVITY);
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
            // TODO Navigate search places screen
        } else {
            WordActivity.start(category, ResultTo.ACTIVITY);
        }
    }

    @Override
    public void onActivityClicked(Activity activity) {
        mPresenter.selectActivity(activity);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onWordRemoved(Word word) {
        mPresenter.removeSelectedWord(word);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onActivityRemoved(Activity activity) {
        mPresenter.removeActivity();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Word word = resolveResult(requestCode, resultCode, data, Word.class, WordActivity.REQUEST_CODE);
        if (word != null) {
            mPresenter.addSelectedWord(word);
            mPresenter.getWordsWithFilter();
        }
    }
}
