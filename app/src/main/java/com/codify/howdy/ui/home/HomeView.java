package com.codify.howdy.ui.home;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Emotion;
import com.codify.howdy.model.Wall;
import com.codify.howdy.ui.base.MvpView;

interface HomeView extends MvpView {

    void onSearchClicked();

    void onChatClicked();

    void onLoaded(Wall wall);

    void onError(ApiError error);

    void onEmotionClicked(Emotion emotion);
}
