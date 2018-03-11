package com.codify.howdy.ui.profileedit;

import android.net.Uri;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

interface ProfileEditView extends MvpView {

    void onPhotoClicked();

    void onPhotoSelected(Uri uri);

    void onSaveClicked();

    void onLoaded(String imagePath);

    void onLoaded(User user);

    void onError(ApiError error);

    void onBackClicked();
}
