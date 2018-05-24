package istanbul.codify.muudy.ui.givevote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import istanbul.codify.muudy.MuudyDialog;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GivePointResponse;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.GivePointLink;
public class GiveVoteDialog extends MuudyDialog implements GiveVoteView {

    private GiveVoteDialogPresenter mPresenter = new GiveVoteDialogPresenter();

    public static GiveVoteDialog newInstance(String sentence, Long id) {
        Bundle args = new Bundle();
        args.putSerializable(String.class.getSimpleName(), sentence);
        args.putSerializable(Long.class.getSimpleName(),id);

        GiveVoteDialog fragment = new GiveVoteDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_givevote_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        String sentence = getSerializable(String.class);
        mPresenter.bind(sentence);

        DeepLinkManager
                .getInstance()
                .nullifyIf(GivePointLink.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onGivePointClicked(Float point) {
        mPresenter.givePoint(getSerializable(Long.class),point);
    }

    @Override
    public void onError(ApiError error) {
        Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onPointGiven(GivePointResponse response) {
        if (response.data.r.toString().equals("ok")) {
            dismiss();
        }

    }

    @Override
    public void onPointChanged(Float point) {

    }
}
