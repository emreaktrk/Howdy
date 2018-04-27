package istanbul.codify.muudy.ui.mention;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Mention;
import istanbul.codify.muudy.model.ResultTo;
import istanbul.codify.muudy.model.User;

import java.util.ArrayList;

public final class MentionActivity extends MuudyActivity implements MentionView {

    public static final int REQUEST_CODE = 654;

    private MentionPresenter mPresenter = new MentionPresenter();

    public static void start(@ResultTo int to) {
        AppCompatActivity activity = (AppCompatActivity) ActivityUtils.getTopActivity();

        Intent starter = new Intent(activity, MentionActivity.class);

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
        return R.layout.layout_mention;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
        mPresenter.getFollowedUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(ArrayList<User> users) {
        mPresenter.bind(users);
    }

    @Override
    public void onUserSearched(String query) {
        mPresenter.search(query);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onMentionClicked(Mention mention) {
        if (mention.isAdded) {
            mPresenter.add(mention.mUser);
        } else {
            mPresenter.remove(mention.mUser);
        }
    }

    @Override
    public void onDoneClicked(ArrayList<User> users) {
        setResult(RESULT_OK, users);
        onBackPressed();
    }

    @Override
    public void onCloseClicked() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }
}
