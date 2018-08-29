package istanbul.codify.muudy.ui.statistic;

import android.support.v4.view.ViewPager;
import android.view.View;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

final class StatisticPresenter extends BasePresenter<StatisticView> {


    @Override
    public void attachView(StatisticView view, View root) {
        super.attachView(view, root);


        ViewPager pager = findViewById(R.id.statistic_pager, ViewPager.class);
        pager.setAdapter(view.create());
        findViewById(R.id.statistic_tab, SmartTabLayout.class).setViewPager(pager);
    }
}
