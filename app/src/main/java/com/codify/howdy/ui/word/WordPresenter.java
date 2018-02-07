package com.codify.howdy.ui.word;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

final class WordPresenter extends BasePresenter<WordView> {

    @Override
    public void attachView(WordView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.word_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.word_search))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(word -> {
                            Logcat.v("Word searched : " + findViewById(R.id.word_search, AppCompatEditText.class).getText().toString());
                            view.onWordySearched(word.toString());
                        }));
    }

    void bind(List<Word> words) {
        WordAdapter adapter = new WordAdapter(words);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(word -> {
                            Logcat.v("Word clicked");
                            mView.onWordSelected(word);
                        }));

        findViewById(R.id.word_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(mRoot.getContext()));
        findViewById(R.id.word_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void bind(Category category) {
        bind(category.words);

        findViewById(R.id.word_title, AppCompatTextView.class).setText(category.words_top_category_text);
    }

    void filter(String query, List<Word> words) {
        Locale locale = new Locale("tr", "TR");

        mDisposables.add(
                Flowable
                        .fromIterable(words)
                        .filter(word -> word.words_word.toLowerCase(locale).startsWith(query.toLowerCase(locale)))
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filtered -> {
                            RecyclerView.Adapter adapter = findViewById(R.id.word_recycler, RecyclerView.class).getAdapter();
                            if (adapter != null && adapter instanceof WordAdapter) {
                                ((WordAdapter) adapter).setFiltered(filtered);
                            }
                        }));

    }

    void filter(String query) {
        RecyclerView.Adapter adapter = findViewById(R.id.word_recycler, RecyclerView.class).getAdapter();
        if (adapter != null && adapter instanceof WordAdapter) {
            List<Word> words = ((WordAdapter) adapter).getWords();
            filter(query, words);
        }
    }

}
