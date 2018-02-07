package com.codify.howdy.ui.compose;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Category;
import com.codify.howdy.ui.base.MvpView;

import java.util.ArrayList;


interface ComposeView extends MvpView {

    void onSendClicked();

    void onSearchClicked();

    void onLoaded(ArrayList<Category> categories);

    void onError(ApiError error);

    void onCategoryClicked(Category category);
}
