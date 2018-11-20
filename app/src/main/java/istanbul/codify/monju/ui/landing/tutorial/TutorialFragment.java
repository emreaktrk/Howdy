package istanbul.codify.monju.ui.landing.tutorial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Tutorial;

public final class TutorialFragment extends MuudyFragment implements TutorialView {

    private TutorialPresenter mPresenter = new TutorialPresenter();

    public static TutorialFragment newInstance(Tutorial tutorial) {
        Bundle args = new Bundle();
        args.putSerializable(tutorial.getClass().getSimpleName(), tutorial);

        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle args(Tutorial tutorial) {
        Bundle args = new Bundle();
        args.putSerializable(tutorial.getClass().getSimpleName(), tutorial);

        return args;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_tutorial;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, this);

        Tutorial tutorial = getSerializable(Tutorial.class);
        if (tutorial != null) {
            mPresenter.bind(tutorial);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }
}
