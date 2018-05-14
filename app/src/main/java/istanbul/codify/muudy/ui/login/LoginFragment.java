package istanbul.codify.muudy.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Credential;
import istanbul.codify.muudy.model.FacebookProfile;
import istanbul.codify.muudy.model.ResultTo;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.createuser.CreateUserActivity;
import istanbul.codify.muudy.ui.forgotpassword.ForgotPasswordActivity;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.register.RegisterFragment;

import java.util.Arrays;


public final class LoginFragment extends MuudyFragment implements LoginView {

    private LoginPresenter mPresenter = new LoginPresenter();
    private CallbackManager mCallback;

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
        mCallback = CallbackManager.Factory.create();

        LoginManager
                .getInstance()
                .registerCallback(mCallback, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        fetchFacebookProfile();
                    }

                    @Override
                    public void onCancel() {
                        // Empty block
                    }

                    @Override
                    public void onError(FacebookException error) {
                        ToastUtils.showShort(error.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();

        LoginManager
                .getInstance()
                .unregisterCallback(mCallback);
    }

    @Override
    public void onLoginClicked(Credential credential) {
        mPresenter.login(credential);
    }

    @Override
    public void onConnectWithFacebookClicked() {
        LoginManager
                .getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onCreateUser(User user) {
        CreateUserActivity.start(ResultTo.FRAGMENT, user);
    }

    @Override
    public void onForgotPasswordClicked() {
        ForgotPasswordActivity.start(null);
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

        MainActivity.start();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallback.onActivityResult(requestCode, resultCode, data);
            return;
        }

        User user = resolveResult(requestCode, resultCode, data, User.class, CreateUserActivity.REQUEST_CODE);
        if (user != null) {
            AccountUtils.login(getContext(), user);

            MainActivity.start();
        }
    }

    private void fetchFacebookProfile() {
        GraphRequest request = GraphRequest
                .newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
                    FacebookProfile facebook = new Gson().fromJson(object.toString(), FacebookProfile.class);
                    loginWithFacebook(facebook);
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,link,location,timezone,updated_time,verified,picture.type(square).width(512).height(512),about,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginWithFacebook(FacebookProfile facebook) {
        mPresenter.bind(facebook);
    }


}
