package istanbul.codify.muudy.ui.compose.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.HowdyDialog;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.event.ShareEvent;
import org.greenrobot.eventbus.EventBus;

public final class ComposeDialog extends HowdyDialog implements ComposeDialogView {

    private ComposeDialogPresenter mPresenter = new ComposeDialogPresenter();

    public static ComposeDialog newInstance(String sentence) {
        Bundle args = new Bundle();
        args.putSerializable(String.class.getSimpleName(), sentence);

        ComposeDialog fragment = new ComposeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_compose;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        String sentence = getSerializable(String.class);
        mPresenter.bind(sentence);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onShareClicked() {
        ShareEvent event = new ShareEvent();
        event.sentence = getSerializable(String.class);
        // TODO Set event properties like visibility etc.

        EventBus
                .getDefault()
                .post(event);

        dismiss();
    }
}
