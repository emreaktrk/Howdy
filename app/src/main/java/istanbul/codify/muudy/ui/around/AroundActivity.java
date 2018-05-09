package istanbul.codify.muudy.ui.around;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public final class AroundActivity extends MuudyActivity implements AroundView {

    private AroundPresenter mPresenter = new AroundPresenter();

    public static void start(@NonNull ArrayList<AroundUsers> around) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, AroundActivity.class);
        starter.putExtra(around.getClass().getSimpleName(), around);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_around;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        List<AroundUsers> around = (ArrayList<AroundUsers>) getSerializable(ArrayList.class);
        mPresenter.bind(around);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onMoreClicked(List<User> users) {
        // TODO Navigate to more users
    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
