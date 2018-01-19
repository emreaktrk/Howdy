package com.codify.howdy.ui.landing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;

public final class LandingFragment extends HowdyFragment implements LandingView {

    private LandingPresenter mPresenter = new LandingPresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_landing;
    }

    public static LandingFragment newInstance() {
        Bundle args = new Bundle();

        LandingFragment fragment = new LandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onLoginClicked() {

    }

    @Override
    public void onRegisterClicked() {

    }
}
