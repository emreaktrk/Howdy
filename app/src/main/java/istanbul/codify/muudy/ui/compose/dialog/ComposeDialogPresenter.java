package istanbul.codify.muudy.ui.compose.dialog;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class ComposeDialogPresenter extends BasePresenter<ComposeDialogView> {

    @Override
    public void attachView(ComposeDialogView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_share))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");

                            view.onShareClicked();
                        }));
    }

    void bind(String sentence) {
        findViewById(R.id.compose_sentence, AppCompatTextView.class).setText(sentence);
    }
}
