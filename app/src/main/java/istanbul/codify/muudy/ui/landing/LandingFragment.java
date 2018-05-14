package istanbul.codify.muudy.ui.landing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.ui.login.LoginFragment;
import istanbul.codify.muudy.ui.register.RegisterFragment;

public final class LandingFragment extends MuudyFragment implements LandingView {

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
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, LoginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onRegisterClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, RegisterFragment.newInstance())
                    .commit();
        }
    }
}
