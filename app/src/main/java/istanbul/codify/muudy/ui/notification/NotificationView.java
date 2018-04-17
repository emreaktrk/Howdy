package istanbul.codify.muudy.ui.notification;

import istanbul.codify.muudy.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface NotificationView extends MvpView {

    FragmentPagerItemAdapter create();
}
