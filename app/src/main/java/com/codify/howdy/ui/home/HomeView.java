package com.codify.howdy.ui.home;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Emotion;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.Wall;
import com.codify.howdy.ui.base.MvpView;

interface HomeView extends MvpView {

    void onSearchClicked();

    void onMessagesClicked();

    void onLoaded(Wall wall);

    void onError(ApiError error);

    void onEmotionClicked(Emotion emotion);

    void onPostClicked(Post post);

    void onLikeClicked(Post post);

    void onCommentClicked(Post post);
}
