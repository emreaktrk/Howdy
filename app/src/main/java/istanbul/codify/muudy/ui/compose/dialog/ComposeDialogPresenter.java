package istanbul.codify.muudy.ui.compose.dialog;

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.PostVisibility;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class ComposeDialogPresenter extends BasePresenter<ComposeDialogView> {

    @Override
    public void attachView(ComposeDialogView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_dialog_share))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");

                            view.onShareClicked();
                        }));
    }

    void bind(String sentence) {
        findViewById(R.id.compose_dialog_sentence, AppCompatTextView.class).setText(sentence);

        ArrayAdapter<PostVisibility> adapter = new ArrayAdapter<>(getContext(), R.layout.md_listitem, R.id.md_title, PostVisibility.values());
        findViewById(R.id.compose_dialog_spinner, AppCompatSpinner.class).setAdapter(adapter);
    }

    PostVisibility getSelectedVisibility() {
        Object visibility = findViewById(R.id.compose_dialog_spinner, AppCompatSpinner.class).getSelectedItem();
        if (visibility instanceof PostVisibility) {
            return (PostVisibility) visibility;
        }

        return PostVisibility.ALL;
    }
}
