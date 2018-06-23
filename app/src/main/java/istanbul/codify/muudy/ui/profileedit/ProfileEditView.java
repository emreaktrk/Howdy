package istanbul.codify.muudy.ui.profileedit;

import android.net.Uri;
import android.view.View;
import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

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
