package com.codify.howdy.ui.mention;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Mention;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

import java.util.ArrayList;

interface MentionView extends MvpView {

    void onLoaded(ArrayList<User> users);

    void onError(ApiError error);

    void onUserSearched(String query);

    void onMentionClicked(Mention mention);

    void onDoneClicked(ArrayList<User> users);

    void onCloseClicked();

}
