package istanbul.codify.muudy.ui.media;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.HowdyBottomSheet;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.MediaEvent;
import istanbul.codify.muudy.model.MediaType;
import org.greenrobot.eventbus.EventBus;

public final class MediaBottomSheet extends HowdyBottomSheet implements MediaView {

    private MediaPresenter mPresenter = new MediaPresenter();

    public static MediaBottomSheet newInstance() {
        return new MediaBottomSheet();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_media_dialog;
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
    public void onMediaTypeSelected(MediaType type) {
        MediaEvent event = new MediaEvent(type);

        EventBus
                .getDefault()
                .post(event);

        dismiss();
    }
}
