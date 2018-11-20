package istanbul.codify.monju.ui.landing.tutorial;

import android.support.v7.widget.AppCompatTextView;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Tutorial;
import istanbul.codify.monju.ui.base.BasePresenter;

final class TutorialPresenter extends BasePresenter<TutorialView> {

    void bind(Tutorial tutorial) {
        findViewById(R.id.tutorial_title, AppCompatTextView.class).setText(tutorial.title);
        findViewById(R.id.tutorial_description, AppCompatTextView.class).setText(tutorial.description);
    }
}
