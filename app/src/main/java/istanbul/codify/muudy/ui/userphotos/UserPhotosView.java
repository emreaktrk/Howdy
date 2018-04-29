package istanbul.codify.muudy.ui.userphotos;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface UserPhotosView extends MvpView {

    FragmentPagerItemAdapter create(User user);

    void onBackClicked();
}
