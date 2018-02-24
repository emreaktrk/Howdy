package com.codify.howdy.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.codify.howdy.R;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.ResultTo;
import com.codify.howdy.model.User;
import com.codify.howdy.model.Wall;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;
import com.codify.howdy.ui.messages.UserMessagesActivity;
import com.codify.howdy.ui.search.UserSearchActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public final class HomeFragment extends NavigationFragment implements HomeView {

    private HomePresenter mPresenter = new HomePresenter();

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        Dexter
                .withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            mPresenter.getWall(getContext());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Empty block
                    }
                })
                .check();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onSearchClicked() {
        UserSearchActivity.start(ResultTo.FRAGMENT);
    }

    @Override
    public void onMessagesClicked() {
        UserMessagesActivity.start();
    }

    @Override
    public void onLoaded(Wall wall) {
        mPresenter.bind(wall);
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User user = resolveResult(requestCode, resultCode, data, User.class, UserSearchActivity.REQUEST_CODE);
    }

    @Override
    public int getSelection() {
        return Navigation.HOME;
    }
}
