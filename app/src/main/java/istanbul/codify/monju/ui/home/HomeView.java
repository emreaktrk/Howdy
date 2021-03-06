package istanbul.codify.monju.ui.home;

import android.graphics.Bitmap;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;

interface HomeView extends MvpView {

    void onSearchClicked();

    void onMessagesClicked();

    void onLoaded(Wall wall);

    void onLoaded(Wall wall,ArrayList<AroundUsers> aroundUsers, Long postId);

    void onPostsLoaded(ArrayList<AroundUsers> aroundUsers, Long postId);

    void onMoreLoaded(ArrayList<Post> posts, More more);

    void onError(ApiError error);

    void onEmotionClicked(Emotion emotion);

    void onPostClicked(Post post);

    void onLikeClicked(Like like);

    void onVideoClicked(Post post);

    void onImageClicked(Post post);

    void onAvatarClicked(Post post);

    void onUserClicked(User user);

    void onFollowClicked(Follow follow);

    void onDeleteClicked(Post post);

    void onMuudyClicked(Post post);

    void onPostDeleted();

    void onRefresh();

    void onMorePage(More more);

    void onLikeCountClicked(Post post);

    void onBlurredImageTaken(ArrayList<AroundUsers> arounds, Bitmap bitmap, long postId);

}
