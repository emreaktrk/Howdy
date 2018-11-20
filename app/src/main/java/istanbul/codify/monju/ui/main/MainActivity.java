package istanbul.codify.monju.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;

import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.DeepLinkManager;
import istanbul.codify.monju.fcm.UpdatePushService;
import istanbul.codify.monju.helper.Pool;
import istanbul.codify.monju.model.AroundUsers;
import istanbul.codify.monju.model.event.HomeReselectEvent;
import istanbul.codify.monju.model.event.OwnProfileEvent;
import istanbul.codify.monju.model.event.PostEvent;
import istanbul.codify.monju.model.event.notification.ProfileEvent;
import istanbul.codify.monju.navigation.Navigation;
import istanbul.codify.monju.ui.compose.ComposeActivity;
import istanbul.codify.monju.ui.home.HomeFragment;
import istanbul.codify.monju.ui.notification.NotificationFragment;
import istanbul.codify.monju.ui.profile.ProfileFragment;
import istanbul.codify.monju.ui.statistic.StatisticFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public final class MainActivity extends MuudyActivity implements MainView, Navigation.IController, EventSupport {

    public Pool<Fragment> mPool = new Pool<Fragment>() {
        @Override
        protected Fragment supply(Class<? extends Fragment> clazz) throws Exception {
            return clazz.newInstance();
        }
    };
    private MainPresenter mPresenter = new MainPresenter();

    HomeFragment homeFragment = new HomeFragment();
    StatisticFragment statisticFragment = new StatisticFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();
        Intent starter = new Intent(context, MainActivity.class);
        ActivityUtils.startActivity(starter);
        ActivityUtils.finishAllActivities();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, homeFragment)
                .commit();

        mPresenter.attachView(this, this);

        UpdatePushService.start();

        boolean isFromPush = getIntent().getBooleanExtra("isFromPush", false);

        if (isFromPush) {
            DeepLink pending = DeepLinkManager
                    .getInstance()
                    .getPending();

            if (pending != null) {
                pending.navigate(this);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostEvent(PostEvent event) {
        ArrayList<AroundUsers> around = event.newPost.aroundUsers;
        mPresenter.openHomeFragment(around,event.newPost.id);
        if (!around.isEmpty()) {

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ProfileEvent event) {
        mPresenter.showNotificationBadge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onHomeClicked(boolean reselect) {
        if (reselect) {
            EventBus
                    .getDefault()
                    .post(new HomeReselectEvent());
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, homeFragment,"1")
                    .addToBackStack(null)
                    .commit();


        }
    }

    @Override
    public void openHome(boolean reselect, ArrayList<AroundUsers> around, Long postId) {

        if (!reselect) {
            HomeFragment fragment = new HomeFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(around.getClass().getSimpleName(), around);
            arguments.putSerializable(postId.getClass().getSimpleName(),postId);
            fragment.setArguments(arguments);


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onStatisticClicked(boolean reselect) {
        if (!reselect) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, statisticFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onComposeClicked() {
        ComposeActivity.start();
    }

    @Override
    public void onNotificationClicked(boolean reselect) {
        if (!reselect) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, notificationFragment)
                    .addToBackStack(null)
                    .commit();
        }

        mPresenter.hideNotificationBadge();
    }

    @Override
    public void onProfileClicked(boolean reselect) {
        if (!reselect) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, profileFragment)
                    .addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void onNavigationSelected(@Navigation int selection) {
        mPresenter.setSelected(selection);
    }

    @Override
    public void setSelected(@Navigation int selection) {
        mPresenter.setSelected(selection);
        switch (selection) {
            case Navigation.HOME:
                onHomeClicked(false);
                return;
            case Navigation.STATISTIC:
                onStatisticClicked(false);
                return;
            case Navigation.NOTIFICATION:
                onNotificationClicked(false);
                return;
            case Navigation.PROFILE:
                onProfileClicked(false);
                return;
            default:
                throw new IllegalArgumentException("Unknown navigation");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOwnProfileEvent(OwnProfileEvent event) {
        mPresenter.setSelected(Navigation.PROFILE);
    }
}
