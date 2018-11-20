package istanbul.codify.monju.ui.profile;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.UserTop;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;
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

    void onLoadedUserTops(ArrayList<UserTop> userTops);

    void onLoaded(List<Post> posts, int selectedIndex);

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onImageClicked(Post post);

    void onVideoClicked(Post post);

    void onDeleteClicked(Post post);

    void onPostDeleted();

    void onError(ApiError error);

    void onFacebookClicked();

    void onTwitterClicked();

    void onInstagramClicked();

    void onEditClicked();

    void onRefresh();

    void onLikeCountClicked(Post post);
}
