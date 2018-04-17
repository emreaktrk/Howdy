package istanbul.codify.muudy.ui.home;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.base.MvpView;

interface HomeView extends MvpView {

    void onSearchClicked();

    void onMessagesClicked();

    void onLoaded(Wall wall);

    void onError(ApiError error);

    void onEmotionClicked(Emotion emotion);

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onVideoClicked(Post post);

    void onImageClicked(Post post);

    void onAvatarClicked(Post post);

    void onUserClicked(User user);

    void onFollowClicked(Follow follow);
}
