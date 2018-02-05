package com.codify.howdy.ui.auth;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codify.howdy.HowdyActivity;
import com.codify.howdy.ui.landing.LandingFragment;

public final class AuthActivity extends HowdyActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AuthActivity.class);
        context.startActivity(starter);
    }

    public static Bundle bundle(Context context, AccountAuthenticatorResponse response) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    protected int getLayoutResId() {
        return NO_ID;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, LandingFragment.newInstance())
                .commit();
    }
}