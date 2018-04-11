package com.codify.howdy.ui.statistic;

import com.codify.howdy.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface StatisticView extends MvpView {

    FragmentPagerItemAdapter create();
}
