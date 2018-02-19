package com.codify.howdy.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;

import java.util.ArrayList;

public final class UserSearchActivity extends HowdyActivity implements UserSearchView {

    private UserSearchPresenter mPresenter = new UserSearchPresenter();

    public static void start(Context context) {
        Intent starter = new Intent(context, UserSearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onUserSearched(String query) {
        mPresenter.search(query);
    }

    @Override
    public void onLoaded(ArrayList<User> users) {
        mPresenter.bind(users);
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
    }
}
