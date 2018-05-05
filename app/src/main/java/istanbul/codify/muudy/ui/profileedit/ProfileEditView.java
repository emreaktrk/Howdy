package istanbul.codify.muudy.ui.profileedit;

import android.net.Uri;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

interface ProfileEditView extends MvpView {

    void onPhotoClicked();

    void onPhotoSelected(Uri uri);

    void onSaveClicked();

    void onChangePasswordClicked();

    void onProfileUpdated();

    void onLoaded(String imagePath);

    void onLoaded(User user);

    void onError(ApiError error);

    void onError(Throwable throwable);

    void onBackClicked();

}
