package istanbul.codify.muudy.ui.word;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.MvpView;

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
