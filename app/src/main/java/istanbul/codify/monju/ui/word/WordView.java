package istanbul.codify.monju.ui.word;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Category;
import istanbul.codify.monju.model.Word;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.List;


interface WordView extends MvpView {

    void onWordSearched(String query);

    void onWordSelected(Word word);

    void onSuggestClicked(String suggest);

    void onSuggested();

    void onLoaded(Category category);

    void onLoaded(List<Word> words);

    void onError(ApiError error);

    void onMentionClicked();

    void onBackClicked();

}
