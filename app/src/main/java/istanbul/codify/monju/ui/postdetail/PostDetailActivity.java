package istanbul.codify.monju.ui.postdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.KeyboardSupport;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.analytics.Analytics;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.deeplink.DeepLinkManager;
import istanbul.codify.monju.deeplink.PostDetailLink;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.UsersScreenMode;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.model.zipper.PostDetail;
import istanbul.codify.monju.ui.photo.PhotoActivity;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import istanbul.codify.monju.ui.users.UsersActivity;
import istanbul.codify.monju.ui.video.VideoActivity;

public final class PostDetailActivity extends MuudyActivity implements PostDetailView, KeyboardSupport {

    private PostDetailPresenter mPresenter = new PostDetailPresenter();

    public static void start(@NonNull Long postId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PostDetailActivity.class);
        starter.putExtra(postId.getClass().getSimpleName(), postId);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_post_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Long postId = getSerializable(Long.class);
        if (postId != null) {
            mPresenter.getPostDetail(postId);
        }

        DeepLinkManager
                .getInstance()
                .nullifyIf(PostDetailLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(@NonNull PostDetail detail) {
        mPresenter.bind(detail);
    }

    @Override
    public void onLoaded() {
        Long postId = getSerializable(Long.class);
        if (postId != null) {
            mPresenter.getPostDetail(postId);
        }
    }

    @Override
    public void onLikeCountClicked(Post post) {
        UsersActivity.start(UsersScreenMode.LIKERS, post.idpost);
    }

    @Override
    public void onCommentImageclicked(Long userId) {
        UserProfileActivity.start(userId);
    }

    @Override
    public void onDeleteComment(Long commentId) {
        mPresenter.deleteComment(commentId);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Throwable error) {
        ToastUtils.showShort(error.getMessage());
    }

    @Override
    public void onLikeClicked(Like like) {
        Long postId = getSerializable(Long.class);
        if (postId == null) {
            return;
        }

        if (like.isChecked) {
            mPresenter.like(postId);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.LIKE);
        } else {
            mPresenter.dislike(postId);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.DISLIKE);
        }
    }

    @Override
    public void onSendClicked(String comment) {
        Long postId = getSerializable(Long.class);
        if (postId != null) {
            mPresenter.send(postId, comment);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.COMMENT);
        }
    }

    @Override
    public void onImageClicked(Post post) {
        PhotoActivity.start(post.post_mediapath);
    }

    @Override
    public void onVideoClicked(Post post) {
        VideoActivity.start(post);
    }

    @Override
    public void onAvatarClicked(Post post) {
        User me = AccountUtils.me(this);
        if (me.iduser == post.iduser){
            UserProfileActivity.startForMe(post.iduser);
        }else{
            UserProfileActivity.start(post.iduser);
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onKeyboard(boolean isOpen) {
        if (isOpen) {
            mPresenter.collapse();
        } else {
            mPresenter.expand();
        }
    }
}
