package istanbul.codify.monju.ui.posts;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.ui.base.MvpView;
import istanbul.codify.monju.ui.home.PostAdapter;

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

    void onDeleteClicked(Post post, PostAdapter adapter);

    void onPostDeleted();

    void onMuudyClicked(Post post);

    void onLikeCountClicked(Post post);
}
