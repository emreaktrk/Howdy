package com.codify.howdy.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;
import com.codify.howdy.model.RegisterForm;


public final class RegisterFragment extends HowdyFragment implements RegisterView {

    private RegisterPresenter mPresenter = new RegisterPresenter();

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_register;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override public void onCloseClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        }
    }

    @Override public void onRegisterClicked(RegisterForm form) {

    }
}
