package istanbul.codify.monju.ui.profileedit;

import android.net.Uri;

import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.MvpView;

interface ProfileEditView extends MvpView {

    void onPhotoClicked(CircleImageView view);

    void onPhoto2Clicked(CircleImageView view,User user);

    void onPhoto3Clicked(CircleImageView view, User user);

    void onPhotoSelected(Uri uri, CircleImageView view);

    void onSaveClicked();

    void onChangePasswordClicked();

    void onProfileUpdated();

    void onLoaded(String imagePath);

    void onError(ApiError error);

    void onError(Throwable throwable);

    void onBackClicked();

}
