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
import com.codify.howdy.model.zipper.PostDetail;

public final class PostDetailActivity extends HowdyActivity implements PostDetailView {

    private PostDetailPresenter mPresenter = new PostDetailPresenter();

    public static void start(@NonNull Post post) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PostDetailActivity.class);
        starter.putExtra(post.getClass().getSimpleName(), post);
        ActivityUtils.startActivity(starter);
    }

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

        Post post = getSerializable(Post.class);
        if (post != null) {
            // TODO Load post
            return;
        }

        Long postId = getSerializable(Long.class);
        if (postId != null) {
            mPresenter.getPost(postId);
            return;
        }
    }

    @Override
    public void onLoaded(@NonNull Post post) {

    }

    @Override
    public void onLoaded(@NonNull PostDetail detail) {

    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Throwable error) {

    }

    @Override
    public void onLikeClicked() {

    }

    @Override
    public void onUnlikeClicked() {

    }

    @Override
    public void onSendClicked(String comment) {
        mPresenter.send(comment);

        Analytics
                .getInstance()
                .custom(Analytics.Events.COMMENT);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
