package istanbul.codify.muudy.ui.postdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.model.zipper.PostDetail;
import istanbul.codify.muudy.ui.photo.PhotoActivity;
import istanbul.codify.muudy.ui.video.VideoActivity;

public final class PostDetailActivity extends MuudyActivity implements PostDetailView {

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
    public void onLoaded(@NonNull Object object) {

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
        }

        Analytics
                .getInstance()
                .custom(Analytics.Events.COMMENT);
    }

    @Override
    public void onImageClicked(Post post) {
        PhotoActivity.start(post);
    }

    @Override
    public void onVideoClicked(Post post) {
        VideoActivity.start(post);
    }

    @Override
    public void onAvatarClicked(Post post) {
        PhotoActivity.start(post.imgpath1);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
