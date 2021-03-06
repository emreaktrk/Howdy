package istanbul.codify.monju.ui.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.ui.login.LoginFragment;
import istanbul.codify.monju.ui.register.RegisterFragment;


public final class WelcomeFragment extends MuudyFragment implements WelcomeView {

    private WelcomePresenter mPresenter = new WelcomePresenter();

    public static WelcomeFragment newInstance() {
        Bundle args = new Bundle();

        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_welcome;
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
    public void onRegisterWithEmailClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, RegisterFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onRegisterWithFacebookClicked() {

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
}
