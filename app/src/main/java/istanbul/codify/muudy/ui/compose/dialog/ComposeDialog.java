package istanbul.codify.muudy.ui.compose.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.MuudyDialog;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.PostVisibility;
import istanbul.codify.muudy.model.event.ShareEvent;
import org.greenrobot.eventbus.EventBus;

public final class ComposeDialog extends MuudyDialog implements ComposeDialogView {

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
        return R.layout.layout_compose_dialog;
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
        event.visibility = mPresenter.getSelectedVisibility();

        EventBus
                .getDefault()
                .post(event);

        dismiss();
    }

    @Override
    public void onFacebookClicked() {
        mPresenter.changeFacebookImage();
    }

    @Override
    public void onTwitterClicked() {
        mPresenter.changeTwitterImage();
    }


}
