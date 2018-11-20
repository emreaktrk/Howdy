package istanbul.codify.monju.ui.statistic;

import istanbul.codify.monju.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface StatisticView extends MvpView {

    FragmentPagerItemAdapter create();


}
