package istanbul.codify.muudy.ui.posts;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Follow;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface PostsView extends MvpView {

    void onLoaded(List<Post> posts);

    void onError(ApiError error);

    void onCloseClicked();

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onImageClicked(Post post);

    void onVideoClicked(Post post);

    void onAvatarClicked(Post post);

    void onUserClicked(User user);

    void onFollowClicked(Follow follow);

    void onDeleteClicked(Post post);

    void onPostDeleted();

    void onMuudyClicked(Post post);
}
