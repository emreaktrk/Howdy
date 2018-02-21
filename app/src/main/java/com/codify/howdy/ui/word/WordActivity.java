package com.codify.howdy.ui.word;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.ResultTo;
import com.codify.howdy.model.Word;

import io.reactivex.annotations.NonNull;

public final class WordActivity extends HowdyActivity implements WordView {

    public static final int REQUEST_CODE = 123;

    private WordPresenter mPresenter = new WordPresenter();

    public static void start(@NonNull Category category, @ResultTo int to) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, WordActivity.class);
        starter.putExtra(category.getClass().getSimpleName(), category);

        switch (to) {
            case ResultTo.ACTIVITY:
                activity.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(activity.getSupportFragmentManager());
                fragment.startActivityForResult(starter, REQUEST_CODE);
                return;
            default:
                throw new IllegalArgumentException("Unknown ResultTo value");
        }
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
    public void onWordSearched(String query) {
        mPresenter.filter(query);
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
