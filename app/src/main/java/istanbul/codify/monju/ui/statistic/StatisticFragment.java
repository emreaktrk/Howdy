package istanbul.codify.monju.ui.statistic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.monju.R;
import istanbul.codify.monju.navigation.Navigation;
import istanbul.codify.monju.navigation.NavigationFragment;
import istanbul.codify.monju.ui.statistic.event.StatisticEventFragment;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public final class StatisticFragment extends NavigationFragment implements StatisticView {

    private StatisticPresenter mPresenter = new StatisticPresenter();
    FragmentPagerItemAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_statistic;
    }

    @Override
    public int getSelection() {
        return Navigation.STATISTIC;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public FragmentPagerItemAdapter create() {
        if (adapter == null){
           adapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(),
                    FragmentPagerItems
                            .with(getContext())
                            .add("Etkinlik", StatisticEventFragment.class)
                            /*.add("Harita", StatisticMapFragment.class)*/
                            .create());
            return adapter;
       }else{
            adapter.notifyDataSetChanged();
            return adapter;
        }
    }

}
