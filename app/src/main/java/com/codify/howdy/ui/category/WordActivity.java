package com.codify.howdy.ui.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;

import io.reactivex.annotations.NonNull;

public final class WordActivity extends HowdyActivity implements WordView {

    public static final int REQUEST_CODE = 123;

    private WordPresenter mPresenter = new WordPresenter();

    public static void start(Activity activity, @NonNull Category category) {
        Intent starter = new Intent(activity, WordActivity.class);
        starter.putExtra(category.getClass().getSimpleName(), category);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_word;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Category category = getSerializable(Category.class);
        if (category != null) {
            mPresenter.bind(category);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onWordySearched(String query) {

    }

    @Override
    public void onWordSelected(Word word) {
        setResult(RESULT_OK, word);
        finish();
    }

    @Override
    public void onBackClicked() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }
}
