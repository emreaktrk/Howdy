package com.codify.howdy.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;

public final class SearchActivity extends HowdyActivity implements SearchView {

    private SearchPresenter mPresenter = new SearchPresenter();

    public static void start(Context context) {
        Intent starter = new Intent(context, SearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onUserSearched(String query) {

    }
}
