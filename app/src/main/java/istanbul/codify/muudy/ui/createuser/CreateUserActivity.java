package istanbul.codify.muudy.ui.createuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ResultTo;
import istanbul.codify.muudy.model.User;

public final class CreateUserActivity extends MuudyActivity implements CreateUserView {

    public static final int REQUEST_CODE = 26;

    private CreateUserPresenter mPresenter = new CreateUserPresenter();

    public static void start(@ResultTo int to, @NonNull User user) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, CreateUserActivity.class);
        starter.putExtra(User.class.getSimpleName(), user);

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
        return R.layout.layout_create_user;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onCreateClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.createUser(user);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.CREATE_USER);
        }
    }

    @Override
    public void onLoaded(User user) {
        setResult(RESULT_OK, user);
        finish();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Exception exception) {
        ToastUtils.showShort(exception.getMessage());
    }
}
