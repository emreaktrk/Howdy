package istanbul.codify.monju.ui.landing;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import istanbul.codify.monju.ui.base.MvpView;

interface LandingView extends MvpView {

    void onLoginClicked();

    void onRegisterClicked();

    FragmentPagerItemAdapter create();
}
