package istanbul.codify.muudy.ui.media;

import istanbul.codify.muudy.model.MediaType;
import istanbul.codify.muudy.ui.base.MvpView;

interface MediaView extends MvpView {

    void onMediaTypeSelected(MediaType type);

    void onMakeProfileImage();
}
