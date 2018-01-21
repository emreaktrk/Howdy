package com.codify.howdy.ui.register;

import android.os.Bundle;

import com.codify.howdy.HowdyFragment;
import com.codify.howdy.R;


public final class RegisterFragment extends HowdyFragment {

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
}
