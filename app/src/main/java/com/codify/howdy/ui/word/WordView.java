package com.codify.howdy.ui.word;

import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.MvpView;


interface WordView extends MvpView {

    void onWordSearched(String query);

    void onWordSelected(Word word);

    void onBackClicked();
}
