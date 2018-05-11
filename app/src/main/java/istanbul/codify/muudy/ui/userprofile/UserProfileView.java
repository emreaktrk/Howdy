package istanbul.codify.muudy.ui.userprofile;

import java.util.List;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.base.MvpView;

interface UserProfileView extends MvpView {

    void onFollowersClicked();

    void onFollowingsClicked();

    void onPostsClicked();

    void onTopsClicked();

    void onGamesClicked();

    void onSeriesClicked();

    void onFilmsClicked();

    void onBooksClicked();

    void onHiddenProfile();

    void onFollowClicked();

    void onUserFollowed();

    void onPictureClicked();

    void onLoaded(User user);

    void onLoadedPosts(List<Post> posts);

    void onLoadedStars(List<Post> stars);

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onImageClicked(Post post);

    void onVideoClicked(Post post);

    void onMuudyClicked(Post post);

    void onError(ApiError error);

    void onFacebookClicked();

    void onTwitterClicked();

    void onInstagramClicked();

    void onBackClicked();

}
