package com.codify.howdy.ui.word;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.MvpView;

import java.util.List;


interface WordView extends MvpView {

    void onWordSearched(String query);

    void onWordSelected(Word word);

    void onLoaded(Category category);

    void onLoaded(List<Word> words);

    void onError(ApiError error);

    void onMentionClicked();

    void onBackClicked();

}
