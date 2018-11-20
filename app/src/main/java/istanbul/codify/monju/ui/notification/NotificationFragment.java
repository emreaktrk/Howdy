package istanbul.codify.monju.ui.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import istanbul.codify.monju.R;
import istanbul.codify.monju.deeplink.DeepLinkManager;
import istanbul.codify.monju.deeplink.GeneralNotificationLink;
import istanbul.codify.monju.navigation.Navigation;
import istanbul.codify.monju.navigation.NavigationFragment;
import istanbul.codify.monju.ui.notification.following.NotificationFollowingFragment;
import istanbul.codify.monju.ui.notification.me.NotificationMeFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public final class NotificationFragment extends NavigationFragment implements NotificationView {

    private NotificationPresenter mPresenter = new NotificationPresenter();
    FragmentPagerItemAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_notification;
    }

    @Override
    public int getSelection() {
        return Navigation.NOTIFICATION;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        DeepLinkManager
                .getInstance()
                .nullifyIf(GeneralNotificationLink.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public FragmentPagerItemAdapter create() {
        if (adapter == null) {
            adapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(),
                    FragmentPagerItems
                            .with(getContext())
                            .add("Sen", NotificationMeFragment.class)
                            .add("Takip Edilen", NotificationFollowingFragment.class)
                            .create());
            return adapter;

        }else{
            return adapter;
        }
    }
}
