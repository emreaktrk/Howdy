package com.codify.howdy.ui.postdetail;

import android.support.annotation.NonNull;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.zipper.PostDetail;
import com.codify.howdy.ui.base.MvpView;

interface PostDetailView extends MvpView {

    void onLoaded(@NonNull Post post);

    void onLoaded(@NonNull PostDetail detail);

    void onError(ApiError error);

    void onError(Throwable error);

    void onLikeClicked();

    void onUnlikeClicked();

    void onSendClicked(String comment);

    void onBackClicked();

}
