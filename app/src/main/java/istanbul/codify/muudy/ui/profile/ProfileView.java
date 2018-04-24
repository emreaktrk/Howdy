package istanbul.codify.muudy.ui.profile;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

interface ProfileView extends MvpView {

    void onPostsClicked();

    void onTopsClicked();

    void onGamesClicked();

    void onSeriesClicked();

    void onFilmsClicked();

    void onBooksClicked();

    void onEditClicked();

    void onLoadedPosts(List<Post> posts);

    void onLoadedStars(List<Post> stars);

    void onError(ApiError error);

}
