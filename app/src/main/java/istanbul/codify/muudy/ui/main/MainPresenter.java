package istanbul.codify.muudy.ui.main;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import android.widget.Space;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

final class MainPresenter extends BasePresenter<MainView> {

    @Override
    public void attachView(MainView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_home))
                        .subscribe(o -> {
                            Logcat.v("Home clicked");

                            boolean reselect = findViewById(R.id.navigation_home).isSelected();
                            view.onHomeClicked(reselect);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_statistic))
                        .subscribe(o -> {
                            Logcat.v("Statistic clicked");

                            boolean reselect = findViewById(R.id.navigation_statistic).isSelected();
                            view.onStatisticClicked(reselect);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_notification))
                        .filter(o -> !findViewById(R.id.navigation_notification).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Notification clicked");

                            boolean reselect = findViewById(R.id.navigation_notification).isSelected();
                            view.onNotificationClicked(reselect);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_profile))
                        .filter(o -> !findViewById(R.id.navigation_profile).isSelected())
                        .subscribe(o -> {
                            Logcat.v("Profile clicked");

                            boolean reselect = findViewById(R.id.navigation_profile).isSelected();
                            view.onProfileClicked(reselect);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.navigation_compose))
                        .subscribe(o -> {
                            Logcat.v("Compose clicked");

                            view.onComposeClicked();
                        }));
    }

    void setSelected(@Navigation int navigation) {
        LinearLayoutCompat bar = findViewById(R.id.navigation_bar);
        for (int position = 0; position < bar.getChildCount(); position++) {
            View child = bar.getChildAt(position);
            if (child instanceof Space || child.getId() == R.id.navigation_compose) {
                continue;
            }
            if (child.getId() == findViewById(R.id.navigation_notificaton_container).getId()){
                findViewById(R.id.navigation_notification).setSelected(findViewById(R.id.navigation_notificaton_container).getId() == navigation);
            }

            child.setSelected(child.getId() == navigation);
        }
    }

    void showNotificationBadge(){
        findViewById(R.id.navigation_notification_dot, AppCompatImageView.class).setVisibility(View.VISIBLE);
    }

    void hideNotificationBadge(){
        findViewById(R.id.navigation_notification_dot, AppCompatImageView.class).setVisibility(View.GONE);
    }
}
