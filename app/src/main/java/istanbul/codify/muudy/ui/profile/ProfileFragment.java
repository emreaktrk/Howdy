package istanbul.codify.muudy.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.ui.profileedit.ProfileEditActivity;


public final class ProfileFragment extends NavigationFragment implements ProfileView {

    private ProfilePresenter mPresenter = new ProfilePresenter();

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_profile;
    }

    @Override
    public int getSelection() {
        return Navigation.PROFILE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, this);

        User me = AccountUtils.me(getContext());
        mPresenter.bind(me);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onEditClicked() {
        ProfileEditActivity.start();
    }
}
