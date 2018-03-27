package com.codify.howdy.ui.postdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.analytics.Analytics;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.zipper.Like;
import com.codify.howdy.model.zipper.PostDetail;

public final class PostDetailActivity extends HowdyActivity implements PostDetailView {

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
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onImageClicked(Post post) {
        // TODO Show image

        Analytics
                .getInstance()
                .custom(Analytics.Events.IMAGE);
    }

    @Override
    public void onVideoClicked(Post post) {
        // TODO Play video

        Analytics
                .getInstance()
                .custom(Analytics.Events.VIDEO);
    }
}
