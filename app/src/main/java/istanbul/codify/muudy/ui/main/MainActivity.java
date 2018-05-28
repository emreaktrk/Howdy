package istanbul.codify.muudy.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.fcm.UpdatePushService;
import istanbul.codify.muudy.helper.Pool;
import istanbul.codify.muudy.model.event.HomeReselectEvent;
import istanbul.codify.muudy.model.event.OwnProfileEvent;
import istanbul.codify.muudy.model.event.notification.ProfileEvent;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.ui.compose.ComposeActivity;
import istanbul.codify.muudy.ui.home.HomeFragment;
import istanbul.codify.muudy.ui.notification.NotificationFragment;
import istanbul.codify.muudy.ui.profile.ProfileFragment;
import istanbul.codify.muudy.ui.statistic.StatisticFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public final class MainActivity extends MuudyActivity implements MainView, Navigation.IController, EventSupport {

    public Pool<Fragment> mPool = new Pool<Fragment>() {
        @Override
        protected Fragment supply(Class<? extends Fragment> clazz) throws Exception {
            return clazz.newInstance();
        }
    };
    private MainPresenter mPresenter = new MainPresenter();

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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                .commit();

        mPresenter.attachView(this, this);

        UpdatePushService.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DeepLink pending = DeepLinkManager
                .getInstance()
                .getPending();

        if (pending != null) {
            pending.navigate(this);
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
                    .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onStatisticClicked(boolean reselect) {
        if (!reselect) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame, mPool.get(StatisticFragment.class))
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
                    .replace(R.id.home_frame, mPool.get(NotificationFragment.class))
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
                    .replace(R.id.home_frame, mPool.get(ProfileFragment.class))
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
