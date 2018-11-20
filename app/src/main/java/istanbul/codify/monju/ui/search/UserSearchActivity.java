package istanbul.codify.monju.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.ResultTo;
import istanbul.codify.monju.model.User;

import java.util.List;

public final class UserSearchActivity extends MuudyActivity implements UserSearchView {

    public static final int REQUEST_CODE = 756;

    private UserSearchPresenter mPresenter = new UserSearchPresenter();

    public static void start(@ResultTo int to) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, UserSearchActivity.class);

        switch (to) {
            case ResultTo.ACTIVITY:
                activity.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(activity.getSupportFragmentManager());
                fragment.startActivityForResult(starter, REQUEST_CODE);
                return;
            default:
                throw new IllegalArgumentException("Unknown ResultTo value");
        }
    }


    public static void startWithFollowedUsers(@ResultTo int to, Long userId) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, UserSearchActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);

        switch (to) {
            case ResultTo.ACTIVITY:
                activity.startActivityForResult(starter, REQUEST_CODE);
                return;
            case ResultTo.FRAGMENT:
                Fragment fragment = FragmentUtils.getTopShow(activity.getSupportFragmentManager());
                fragment.startActivityForResult(starter, REQUEST_CODE);
                return;
            default:
                throw new IllegalArgumentException("Unknown ResultTo value");
        }
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Long userId = getSerializable(Long.class);
        if (userId != null) {
            mPresenter.getFollowedUsers(userId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onCloseClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onUserSearched(String query) {
        mPresenter.search(query);
    }

    @Override
    public void onUserClicked(User user) {
        setResult(RESULT_OK, user);
        finish();
    }

    @Override
    public void onLoaded(List<User> users) {
        mPresenter.bind(users);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }
}
