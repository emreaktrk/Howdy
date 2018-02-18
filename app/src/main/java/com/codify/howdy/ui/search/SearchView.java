package com.codify.howdy.ui.search;

import com.codify.howdy.ui.base.MvpView;

interface SearchView extends MvpView {

    void onCloseClicked();

    void onUserSearched(String query);
}
