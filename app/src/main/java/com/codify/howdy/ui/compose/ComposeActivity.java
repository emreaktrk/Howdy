package com.codify.howdy.ui.compose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Category;
import com.codify.howdy.model.Emotion;
import com.codify.howdy.ui.category.WordActivity;

import java.util.ArrayList;


public final class ComposeActivity extends HowdyActivity implements ComposeView {

    private ComposePresenter mPresenter = new ComposePresenter();

    public static void start(Context context) {
        Intent starter = new Intent(context, ComposeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_compose;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
        mPresenter.getWords();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onEmotionClicked(Emotion emotion) {

    }

    @Override
    public void onSendClicked() {

    }

    @Override
    public void onLoaded(ArrayList<Category> categories) {
        WordActivity.start(this, categories.get(0));
    }

    @Override
    public void onError(ApiError error) {

    }
}
