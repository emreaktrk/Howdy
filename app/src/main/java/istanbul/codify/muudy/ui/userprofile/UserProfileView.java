package istanbul.codify.muudy.ui.userprofile;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.UserTop;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.base.MvpView;
import istanbul.codify.muudy.view.FollowButton;

import java.util.ArrayList;
import java.util.List;

interface UserProfileView extends MvpView {

    void onMessageClicked();

    void onMuudyClicked();

    void onMuudySent();

    void onFollowersClicked();

    void onFollowingsClicked();

    void onMoreClicked();

    void onPostsClicked();

    void onTopsClicked();

    void onGamesClicked();

    void onSeriesClicked();

    void onFilmsClicked();

    void onBooksClicked();

    void onFollowClicked(FollowButton compound);

    void onPictureClicked();

    void onLoaded(User user, Boolean isForPosts);

    void onLoadedPosts(List<Post> posts);

    void onLoadedUserTops(ArrayList<UserTop> userTops);

    void onLoadedStars(List<Post> stars);

//    void onLoaded(List<Post> posts, int selectedIndex);

    void onRefresh();

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
