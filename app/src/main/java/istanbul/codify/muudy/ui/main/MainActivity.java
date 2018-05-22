package istanbul.codify.muudy.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.fcm.FCMListenerService;
import istanbul.codify.muudy.fcm.UpdatePushService;
import istanbul.codify.muudy.helper.Pool;
import istanbul.codify.muudy.model.NotificationActionType;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.ui.compose.ComposeActivity;
import istanbul.codify.muudy.ui.home.HomeFragment;
import istanbul.codify.muudy.ui.messages.UserMessagesActivity;
import istanbul.codify.muudy.ui.notification.NotificationFragment;
import istanbul.codify.muudy.ui.profile.ProfileFragment;
import istanbul.codify.muudy.ui.statistic.StatisticFragment;


public final class MainActivity extends MuudyActivity implements MainView, Navigation.IController {

    private MainPresenter mPresenter = new MainPresenter();
    private Pool<Fragment> mPool = new Pool<Fragment>() {
        @Override
        protected Fragment supply(Class<? extends Fragment> clazz) throws Exception {
            return clazz.newInstance();
        }
    };

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

        handlePushNotification();

        UpdatePushService.start();
    }

    private void handlePushNotification() {

        Intent intent = getIntent();

        int itemId = intent.getIntExtra(FCMListenerService.NOTIFICATION_ITEMID, 0);
        NotificationActionType actionType = getNotificationActionType(intent.getIntExtra(FCMListenerService.NOTIFICATION_ACTIONTYPE, 0));

        if (actionType != null) {
            switch (actionType) {
                case MESSAGE:
                    Context context = Utils.getApp().getApplicationContext();

                    Intent starter = new Intent(context, UserMessagesActivity.class);
                    starter.putExtra(FCMListenerService.NOTIFICATION_ITEMID, itemId);
                    starter.putExtra(FCMListenerService.NOTIFICATION_ACTIONTYPE, actionType.ordinal());
                    ActivityUtils.startActivity(starter);
                    break;
                case LIKE:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                            .commit();
                    break;
                case FOLLOW:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(NotificationFragment.class))
                            .addToBackStack(null)
                            .commit();
                    break;
                case FOLLOW_REQUEST:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(NotificationFragment.class))
                            .addToBackStack(null)
                            .commit();
                    break;
                case TAG:
                    break;
                case COMMENT:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                            .commit();
                    break;
                case SAY_HI:
                    break;
                case POST:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(HomeFragment.class))
                            .commit();
                    break;
                case ANSWER_HI:
                    break;
                case GIVE_VOTE:
                    break;
                case WEEK_TOP_USERS:
                    break;
                case GENERAL_ANNOUNCE:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_frame, mPool.get(NotificationFragment.class))
                            .addToBackStack(null)
                            .commit();
                    break;
                case MESSAGE_READED:
                    break;
                case ACTIVITY_REMINDER:
                    break;

                default:
                    break;
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onHomeClicked(boolean reselect) {
        if (reselect) {
            // TODO Event
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
                onHomeClicked(false);
                return;
            case Navigation.PROFILE:
                onProfileClicked(false);
                return;
            default:
                throw new IllegalArgumentException("Unknown navigation");
        }
    }
}
