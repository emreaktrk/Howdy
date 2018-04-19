package istanbul.codify.muudy.ui.word;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetWordsRequest;
import istanbul.codify.muudy.api.pojo.request.GetWordsWithFilterRequest;
import istanbul.codify.muudy.api.pojo.request.SuggestRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetWordsResponse;
import istanbul.codify.muudy.api.pojo.response.GetWordsWithFilterResponse;
import istanbul.codify.muudy.api.pojo.response.SuggestResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .skip(1)
                        .subscribe(word -> {
                            Logcat.v("Word searched : " + findViewById(R.id.word_search, AppCompatEditText.class).getText().toString());

                            view.onWordSearched(word.toString());
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.word_mention))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(word -> {
                            Logcat.v("Mention clicked");

                            view.onMentionClicked();
                        }));
    }

    void bind(List<Word> words) {
        findViewById(R.id.word_title, AppCompatTextView.class).setText("Arama");

        WordAdapter adapter = new WordAdapter(words);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(word -> {
                            Logcat.v("Word clicked");

                            mView.onWordSelected(word);
                        }));

        mDisposables.add(
                adapter
                        .suggestClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(o -> findViewById(R.id.word_search, AppCompatEditText.class) == null)
                        .subscribe(o -> {
                            Logcat.v("Suggest clicked");

                            AppCompatEditText search = findViewById(R.id.word_search, AppCompatEditText.class);
                            String suggest = search.getText().toString();
                            mView.onSuggestClicked(suggest);
                        }));


        findViewById(R.id.word_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(mRoot.getContext()));
        findViewById(R.id.word_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void bind(Category category) {
        bind(category.words);

        findViewById(R.id.word_title, AppCompatTextView.class).setText(category.words_top_category_text);
        findViewById(R.id.word_mention).setVisibility(category.isMeeting() ? View.VISIBLE : View.GONE);
    }

    void getWords(Category category) {
        GetWordsRequest request = new GetWordsRequest(category.id_word_top_category);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWords(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsResponse>() {
                            @Override
                            protected void success(GetWordsResponse response) {
                                mView.onLoaded(response.data.get(0));
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void getWordsWithFilter(long[] ids, @Nullable Activity activity) {
        GetWordsWithFilterRequest request = new GetWordsWithFilterRequest(activity == null ? 0 : activity.idactivities, ids);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWordsWithFilter(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsWithFilterResponse>() {
                            @Override
                            protected void success(GetWordsWithFilterResponse response) {
                                ArrayList<Word> words = new ArrayList<>();
                                for (Category category : response.data.topCategories) {
                                    for (Word word : category.words) {
                                        word.word_top_category_text = category.words_top_category_text;
                                    }

                                    words.addAll(category.words);
                                }

                                mView.onLoaded(words);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
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
                                ((WordAdapter) adapter).setFiltered(filtered, !TextUtils.isEmpty(query));
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

    void suggest(String word, @Nullable Category category) {
        SuggestRequest request = new SuggestRequest(word, category == null ? 0 : category.id_word_top_category);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .suggest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SuggestResponse>() {
                            @Override
                            protected void success(SuggestResponse response) {
                                mView.onSuggested();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
