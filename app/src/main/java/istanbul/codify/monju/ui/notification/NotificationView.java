package istanbul.codify.monju.ui.notification;

import istanbul.codify.monju.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface NotificationView extends MvpView {

    FragmentPagerItemAdapter create();
}
