package istanbul.codify.monju.ui.userphotos;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

interface UserPhotosView extends MvpView {

    FragmentPagerItemAdapter create(User user);

    void onBackClicked();
}
