package com.codify.howdy.ui.category;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

final class WordPresenter extends BasePresenter<WordView> {

    @Override
    public void attachView(WordView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.word_back))
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.word_search))
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .subscribe(o -> {
                            Logcat.v("Word searched : " + findViewById(R.id.word_search, AppCompatEditText.class).getText().toString());
                            view.onBackClicked();
                        }));
    }

    void bind(ArrayList<Word> list) {
        WordAdapter adapter = new WordAdapter(list);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .subscribe(word -> {
                            Logcat.v("Word clicked");
                            mView.onWordSelected(word);
                        }));
    }

    void bind(Category category) {
        bind(category.words);

        findViewById(R.id.word_title, AppCompatTextView.class).setText(category.words_top_category_text);
    }

}
