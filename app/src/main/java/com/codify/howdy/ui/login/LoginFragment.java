package com.codify.howdy.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Credential;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.main.MainActivity;
import com.codify.howdy.ui.welcome.WelcomeFragment;


public final class LoginFragment extends HowdyFragment implements LoginView {

    private LoginPresenter mPresenter = new LoginPresenter();

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_login;
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
    public void onLoginClicked(Credential credential) {
        mPresenter.login(credential);
    }

    @Override
    public void onConnectWithFacebookClicked() {

    }

    @Override
    public void onForgotPasswordClicked() {

    }

    @Override
    public void onRegisterClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, WelcomeFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onCloseClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        }
    }

    @Override
    public void onLogin(User user) {
        AccountUtils.login(getContext(), user);

        MainActivity.start(getActivity());
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
    }
}