package istanbul.codify.monju.ui.postdetail;

import android.support.annotation.NonNull;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.model.zipper.PostDetail;
import istanbul.codify.monju.ui.base.MvpView;

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

    void onCommentImageclicked(Long userId);

    void onDeleteComment(Long commentId);
}
