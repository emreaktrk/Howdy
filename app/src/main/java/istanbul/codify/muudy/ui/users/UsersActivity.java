package istanbul.codify.muudy.ui.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.view.FollowButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UsersActivity extends MuudyActivity implements UsersView {

    private UsersPresenter mPresenter = new UsersPresenter();
    private CallbackManager mCallback;

    public static void start(@Nullable Emotion emotion) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        if (emotion != null) {
            starter.putExtra(emotion.getClass().getSimpleName(), emotion);
            starter.putExtra(String.class.getSimpleName(), UsersScreenMode.AROUND_WITH_EMOTION);
        }
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull User user, @NonNull @UsersScreenMode String mode) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), mode);
        starter.putExtra(User.class.getSimpleName(), user);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull ArrayList<User> users) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), UsersScreenMode.USERS);
        starter.putExtra(ArrayList.class.getSimpleName(), users);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull ArrayList<User> users, boolean isAround) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), UsersScreenMode.AROUND);
        starter.putExtra(ArrayList.class.getSimpleName(), users);
        ActivityUtils.startActivity(starter);
    }


    public static void start(@NonNull @UsersScreenMode String mode) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), mode);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull @UsersScreenMode String mode, Long postId) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, UsersActivity.class);
        starter.putExtra(String.class.getSimpleName(), mode);
        starter.putExtra(Long.class.getSimpleName(),postId);
        ActivityUtils.startActivity(starter);
    }





    @Override
    protected int getLayoutResId() {
        return R.layout.layout_users;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        User user = getSerializable(User.class);
        String mode = getSerializable(String.class);
        mPresenter.bind(mode);

        if (!StringUtils.isEmpty(mode)) {
            switch (mode) {
                case UsersScreenMode.FOLLOWER:
                    if (user != null) {
                        mPresenter.getFollowedUsers(user);
                    }

                    return;
                case UsersScreenMode.FOLLOWING:
                    if (user != null) {
                        mPresenter.getFollowers(user);
                    }

                    return;
                case UsersScreenMode.CONTACTS:
                    mPresenter.getContacts(this);
                    return;
                case UsersScreenMode.AROUND_WITH_EMOTION:
                    Emotion emotion = getSerializable(Emotion.class);
                    mPresenter.aroundUsers(emotion);
                    return;
                case UsersScreenMode.USERS:
                    ArrayList<User> users = getSerializable(ArrayList.class);
                    mPresenter.bind(users);
                    return;
                case UsersScreenMode.AROUND:
                    ArrayList<User> mUsers = getSerializable(ArrayList.class);
                    mPresenter.bind(mUsers);
                    return;
                case UsersScreenMode.LIKERS:
                    Long postId = getSerializable(Long.class);
                    mPresenter.getLikers(postId);
                    return;
                case UsersScreenMode.FACEBOOK:
                    if (AccessToken.getCurrentAccessToken() == null) {
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
                                        finish();
                                    }

                                    @Override
                                    public void onError(FacebookException error) {
                                        ToastUtils.showShort(error.getMessage());

                                        finish();
                                    }
                                });

                        LoginManager
                                .getInstance()
                                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
                    } else {
                        mPresenter.getFacebookFriends();
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Not implemented");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onUserSearched(String query) {

    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }

    @Override
    public void onFollowClicked(Follow follow) {
        switch (follow.mCompound.getState()) {
            case FOLLOW:
                mPresenter.follow(follow.mUser);
                if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                    follow.mCompound.setState(FollowButton.State.UNFOLLOW);
                } else {
                    follow.mCompound.setState(FollowButton.State.REQUEST_CANCEL);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case UNFOLLOW:
                new AlertDialog
                        .Builder(this)
                        .setMessage("Takip Etme?")
                        .setPositiveButton("Evet", (dialogInterface, which) -> {
                            mPresenter.unfollow(follow.mUser);
                            if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                                follow.mCompound.setState(FollowButton.State.FOLLOW);
                            } else {
                                follow.mCompound.setState(FollowButton.State.REQUEST);
                            }

                            Analytics
                                    .getInstance()
                                    .custom(Analytics.Events.UNFOLLOW);
                        })
                        .setNegativeButton("Hayir", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
                return;
            case REQUEST:
                mPresenter.requestFollow(follow.mUser);
                follow.mCompound.setState(FollowButton.State.REQUEST_CANCEL);

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.FOLLOW);
                return;
            case REQUEST_CANCEL:
                mPresenter.cancelRequestFollow(follow.mUser);
                if (follow.mUser.isprofilehidden == ProfileVisibility.VISIBLE) {
                    follow.mCompound.setState(FollowButton.State.FOLLOW);
                } else {
                    follow.mCompound.setState(FollowButton.State.REQUEST);
                }

                Analytics
                        .getInstance()
                        .custom(Analytics.Events.UNFOLLOW);
                return;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onLoaded(List<User> users) {
        mPresenter.bind(users);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onCloseClicked() {
        finish();
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
        mPresenter.update(facebook);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallback.onActivityResult(requestCode, resultCode, data);
        }
    }
}
