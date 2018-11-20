package istanbul.codify.monju.ui.media;

import istanbul.codify.monju.model.MediaType;
import istanbul.codify.monju.ui.base.MvpView;

interface MediaView extends MvpView {

    void onMediaTypeSelected(MediaType type);

    void onMakeProfileImage();
}
