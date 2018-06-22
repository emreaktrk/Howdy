package istanbul.codify.muudy.ui.postdetail;

import android.support.annotation.NonNull;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.model.zipper.PostDetail;
import istanbul.codify.muudy.ui.base.MvpView;

interface PostDetailView extends MvpView {

    void onLoaded(@NonNull PostDetail detail);

    void onLoaded();

    void onError(ApiError error);

    void onError(Throwable error);

    void onLikeClicked(Like like);

    void onSendClicked(String comment);

    void onImageClicked(Post post);

    void onVideoClicked(Post post);

    void onAvatarClicked(Post post);

    void onBackClicked();

    void onLikeCountClicked(Post post);
}
