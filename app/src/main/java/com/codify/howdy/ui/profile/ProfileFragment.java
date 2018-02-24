package com.codify.howdy.ui.profile;

import com.codify.howdy.R;
import com.codify.howdy.navigation.Navigation;
import com.codify.howdy.navigation.NavigationFragment;


public final class ProfileFragment extends NavigationFragment implements ProfileView {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_profile;
    }

    @Override
    public int getSelection() {
        return Navigation.PROFILE;
    }
}
