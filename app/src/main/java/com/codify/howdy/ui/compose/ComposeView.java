package com.codify.howdy.ui.compose;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Activity;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Word;
import com.codify.howdy.ui.base.MvpView;

import java.util.ArrayList;


interface ComposeView extends MvpView {

    void onSendClicked();

    void onSearchClicked();

    void onLoaded(ArrayList<Category> categories, ArrayList<Activity> activities);

    void onLoaded(ArrayList<Category> filtered);

    void onError(ApiError error);

    void onCategoryClicked(Category category);

    void onWordRemoved(Word word);
}
