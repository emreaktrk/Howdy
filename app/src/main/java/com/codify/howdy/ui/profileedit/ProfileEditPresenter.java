package com.codify.howdy.ui.profileedit;

import android.Manifest;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.UploadImageRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.UploadImageResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

final class ProfileEditPresenter extends BasePresenter<ProfileEditView> {

    private Uri mPhoto;

    @Override
    public void attachView(ProfileEditView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_save))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Save clicked");
                            view.onSaveClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_picture))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Photo clicked");
                            view.onPhotoClicked();
                        }));
    }

    void uploadImage() {
        // TODO Convert image to Base64

        UploadImageRequest request = new UploadImageRequest(null);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .uploadImage(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<UploadImageResponse>() {
                            @Override
                            protected void success(UploadImageResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void selectPhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<List<Uri>>>) granted -> granted ? RxGallery.gallery(activity, false).toObservable() : Observable.empty())
                        .subscribe(uris -> {
                            Uri uri = uris.get(0);
                            Logcat.v("Selected uri for photo is " + uri.toString());
                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void bind(Uri photo) {
        mPhoto = photo;

        Picasso
                .with(getContext())
                .load(mPhoto)
                .into(findViewById(R.id.profile_edit_picture, CircleImageView.class));
    }

    void bind(User user) {
        findViewById(R.id.profile_edit_fullname, TextInputEditText.class).setText(user.namesurname);
        findViewById(R.id.profile_edit_username, TextInputEditText.class).setText(user.username);
        findViewById(R.id.profile_edit_email, TextInputEditText.class).setText(user.email);

        Picasso
                .with(getContext())
                .load(user.imgpath1)
                .into(findViewById(R.id.profile_edit_picture, CircleImageView.class));
    }
}
