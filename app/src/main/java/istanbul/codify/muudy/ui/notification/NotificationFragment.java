package istanbul.codify.muudy.ui.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.navigation.Navigation;
import istanbul.codify.muudy.navigation.NavigationFragment;
import istanbul.codify.muudy.ui.notification.following.NotificationFollowingFragment;
import istanbul.codify.muudy.ui.notification.me.NotificationMeFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public final class NotificationFragment extends NavigationFragment implements NotificationView {

    private NotificationPresenter mPresenter = new NotificationPresenter();

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public FragmentPagerItemAdapter create() {
        return new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                FragmentPagerItems
                        .with(getContext())
                        .add("Takip Edilen", NotificationFollowingFragment.class)
                        .add("Sen", NotificationMeFragment.class)
                        .create());
    }
}
