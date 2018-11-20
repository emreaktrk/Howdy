package istanbul.codify.monju.ui.word;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import io.reactivex.annotations.NonNull;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.ui.mention.MentionActivity;

import java.util.ArrayList;
import java.util.List;

public final class WordActivity extends MuudyActivity implements WordView {

    public static final int REQUEST_CODE = 123;

    private WordPresenter mPresenter = new WordPresenter();

    public static void start(@ResultTo int to, @NonNull Category category) {
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

    public static void start(@ResultTo int to, @NonNull Long categoryId) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, WordActivity.class);
        Category category = new Category();
        category.id_word_top_category = categoryId;
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

    public static void start(@Nullable List<? extends Selectable> selecteds, @ResultTo int to) {
        AppCompatActivity compat = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(compat, WordActivity.class);
        if (selecteds != null) {
            long[] ids = new long[selecteds.size()];
            for (int i = 0; i < selecteds.size(); i++) {
                Selectable selected = selecteds.get(i);

                if (selected instanceof Word) {
                    Word word = (Word) selected;
                    ids[i] = word.words_top_category_id;
                }

                if (selected instanceof Activity) {
                    starter.putExtra(Activity.class.getSimpleName(), (Activity) selected);
                }
            }
            starter.putExtra(ids.getClass().getSimpleName(), ids);
        }

        switch (to) {
            case ResultTo.ACTIVITY:
                compat.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(compat.getSupportFragmentManager());
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
            mPresenter.getWords(category);
        } else {
            Activity activity = getSerializable(Activity.class);
            long[] ids = getLongArray();
            mPresenter.getWordsWithFilter(ids, activity);
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
    public void onSuggestClicked(String word) {
        Category category = getSerializable(Category.class);
        mPresenter.suggest(word, category);
    }

    @Override
    public void onSuggested() {
        ToastUtils.showShort("Kelime önerin başarıyla gönderildi");
    }

    @Override
    public void onLoaded(Category category) {
        mPresenter.bind(category);
    }

    @Override
    public void onLoaded(List<Word> words) {
        mPresenter.bind(words);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onMentionClicked() {
        MentionActivity.start(ResultTo.ACTIVITY);
    }

    @Override
    public void onBackClicked() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }

    @SuppressWarnings({"unchecked", "InstantiatingObjectToGetClassObject"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Selectable> users = resolveResult(requestCode, resultCode, data, new ArrayList<User>().getClass(), MentionActivity.REQUEST_CODE);
        if (users != null && !users.isEmpty()) {
            setResult(RESULT_OK, users);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
