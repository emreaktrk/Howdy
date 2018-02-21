package com.codify.howdy.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.codify.howdy.HowdyActivity;
import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.ResultTo;
import com.codify.howdy.model.User;

import java.util.ArrayList;

public final class UserSearchActivity extends HowdyActivity implements UserSearchView {

    public static final int REQUEST_CODE = 756;

    private UserSearchPresenter mPresenter = new UserSearchPresenter();

    public static void start(@ResultTo int to) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, UserSearchActivity.class);

        switch (to) {
            case ResultTo.ACTIVITY:
                activity.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(activity.getSupportFragmentManager());
                fragment.startActivityForResult(starter, REQUEST_CODE);
                return;
            default:
                throw new IllegalArgumentException("Unknown ResultTo value");
        }
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
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onUserSearched(String query) {
        mPresenter.search(query);
    }

    @Override
    public void onUserClicked(User user) {
        setResult(RESULT_OK, user);
        finish();
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
