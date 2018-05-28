package istanbul.codify.muudy.ui.WeeklyTopUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.WeeklyTopLink;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.WeeklyTopUser;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;

import java.util.ArrayList;

public class WeeklyTopUsersActivity extends MuudyActivity implements WeeklyTopUsersView {

    private WeeklyTopUsersPresenter mPresenter = new WeeklyTopUsersPresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, WeeklyTopUsersActivity.class);
        ActivityUtils.startActivity(starter);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.layout_weekly_top_users;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(this,this);
        mPresenter.getWeeklyTopUsers();

        DeepLinkManager
                .getInstance()
                .nullifyIf(WeeklyTopLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void onLoaded(ArrayList<WeeklyTopUser> weeklyTopUsers) {
        mPresenter.bind(weeklyTopUsers);
    }

    @Override
    public void onUserPhotoClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }

    @Override
    public void onCloseClick() {
        onBackPressed();
    }
}
