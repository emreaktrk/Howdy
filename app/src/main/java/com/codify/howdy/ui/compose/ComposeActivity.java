package com.codify.howdy.ui.compose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.word.WordActivity;

import java.util.ArrayList;


public final class ComposeActivity extends HowdyActivity implements ComposeView {

    private ComposePresenter mPresenter = new ComposePresenter();

    public static void start(Context context) {
        Intent starter = new Intent(context, ComposeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_compose;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
        mPresenter.getWords();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked() {

    }

    @Override
    public void onSearchClicked() {

    }

    @Override
    public void onLoaded(ArrayList<Category> categories) {
        mPresenter.bind(categories);
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClicked(Category category) {
        WordActivity.start(this, category);
    }

    @Override
    public void onWordRemoved(Word word) {
        mPresenter.removeSelectedWord(word);
        mPresenter.getWordsWithFilter();
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
