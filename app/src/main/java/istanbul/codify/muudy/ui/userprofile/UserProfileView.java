package istanbul.codify.muudy.ui.userprofile;

import java.util.List;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface UserProfileView extends MvpView {

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

    void onError(ApiError error);

    void onBackClicked();
}
