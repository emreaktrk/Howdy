package istanbul.codify.muudy.ui.profile;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface ProfileView extends MvpView {

    void onFollowersClicked();

    void onFollowingsClicked();

    void onPictureClicked();

    void onPostsClicked();

    void onTopsClicked();

    void onGamesClicked();

    void onSeriesClicked();

    void onFilmsClicked();

    void onBooksClicked();

    void onSettingsClicked();

    void onLoadedPosts(List<Post> posts);

    void onLoadedStars(List<Post> stars);

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onImageClicked(Post post);

    void onVideoClicked(Post post);

    void onDeleteClicked(Post post);

    void onPostDeleted();

    void onError(ApiError error);

}
