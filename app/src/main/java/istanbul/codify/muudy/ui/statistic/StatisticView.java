package istanbul.codify.muudy.ui.statistic;

import istanbul.codify.muudy.model.EmojiLocation;
import istanbul.codify.muudy.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface StatisticView extends MvpView {

    FragmentPagerItemAdapter create();


}
