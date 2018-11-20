package istanbul.codify.monju.ui.landing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Tutorial;
import istanbul.codify.monju.ui.landing.tutorial.TutorialFragment;
import istanbul.codify.monju.ui.login.LoginFragment;
import istanbul.codify.monju.ui.register.RegisterFragment;

public final class LandingFragment extends MuudyFragment implements LandingView {

    private LandingPresenter mPresenter = new LandingPresenter();

    public static LandingFragment newInstance() {
        Bundle args = new Bundle();

        LandingFragment fragment = new LandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_landing;
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
    public void onLoginClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, LoginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onRegisterClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, RegisterFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public FragmentPagerItemAdapter create() {
        return new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                FragmentPagerItems
                        .with(getContext())
                        .add("0", TutorialFragment.class, TutorialFragment.args(new Tutorial("Yeni İnsanlar", "Çevrende seninle aynı paylaşımı yapan insanlarla tanış")))
                        .add("1", TutorialFragment.class, TutorialFragment.args(new Tutorial("Duygu Haritası", "Harita sekmesine girerek en mutlu ve en heyecanlı bölgeleri keşfet!")))
                        .add("2", TutorialFragment.class, TutorialFragment.args(new Tutorial("Trend", "Başlıkların altındaki kelimeler kullanıcıların yaptığı paylaşımlara göre sürekli olarak güncellenir. O an neyin trend olduğunu görebilmek için tek yapman gereken başlığa tıklamak.")))
                        .add("3", TutorialFragment.class, TutorialFragment.args(new Tutorial("Puanlar", "Dizi, film, kitap, oyun gibi başlıklarından yapacağın paylaşımlarda, 2 saat sonra gelecek bildirimle puanlamanı yap ve seni takip edenleri durumdan haberdar et!")))
                        .create());
    }
}
