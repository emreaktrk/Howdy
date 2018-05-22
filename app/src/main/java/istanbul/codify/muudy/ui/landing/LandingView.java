package istanbul.codify.muudy.ui.landing;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import istanbul.codify.muudy.ui.base.MvpView;

interface LandingView extends MvpView {

    void onLoginClicked();

    void onRegisterClicked();

    FragmentPagerItemAdapter create();
}
