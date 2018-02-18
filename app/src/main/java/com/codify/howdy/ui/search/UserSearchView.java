package com.codify.howdy.ui.search;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

import java.util.ArrayList;

interface UserSearchView extends MvpView {

    void onCloseClicked();

    void onUserSearched(String query);

    void onLoaded(ArrayList<User> users);

    void onError(ApiError error);
}
