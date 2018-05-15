package istanbul.codify.muudy.ui.profileedit;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.UpdateProfileRequest;
import istanbul.codify.muudy.api.pojo.request.UploadImageRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.UpdateProfileResponse;
import istanbul.codify.muudy.api.pojo.response.UploadImageResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.ProfileImageType;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

final class ProfileEditPresenter extends BasePresenter<ProfileEditView> {

    private Uri mPhoto1;
    private Uri mPhoto2;
    private Uri mPhoto3;

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
                        .clicks(findViewById(R.id.profile_edit_picture_1))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Photo clicked");

                            view.onPhotoClicked(findViewById(R.id.profile_edit_picture_1, CircleImageView.class));
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_picture_2))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Photo clicked");

                            view.onPhotoClicked(findViewById(R.id.profile_edit_picture_2, CircleImageView.class));
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_picture_3))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Photo clicked");

                            view.onPhotoClicked(findViewById(R.id.profile_edit_picture_3, CircleImageView.class));
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit_password))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Change password clicked");

                            view.onChangePasswordClicked();
                        }));
    }

    private String getMediaData(Uri photo) {
        if (photo == null) {
            return null;
        }

        try {
            InputStream input = getContext().getContentResolver().openInputStream(photo);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            byte[] bytes = output.toByteArray();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            Logcat.e(e);

            return null;
        }
    }

    void selectPhoto(@NonNull AppCompatActivity activity, CircleImageView view) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<List<Uri>>>) granted -> granted ? RxGallery.gallery(activity, false).toObservable() : Observable.empty())
                        .subscribe(uris -> {
                            Uri uri = uris.get(0);
                            Logcat.v("Selected uri for photo is " + uri.toString());
                            mView.onPhotoSelected(uri, view);
                        })
        );
    }

    void capturePhoto(@NonNull AppCompatActivity activity, CircleImageView view) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<Uri>>) granted -> granted ? RxGallery.photoCapture(activity).toObservable() : Observable.empty())
                        .subscribe(uri -> {
                            Logcat.v("Selected uri for photo is " + uri.toString());

                            mView.onPhotoSelected(uri, view);
                        })
        );
    }

    void bind(Uri photo, CircleImageView view) {
        Picasso
                .with(getContext())
                .load(photo)
                .placeholder(R.drawable.ic_avatar)
                .into(view);

        switch (view.getId()) {
            case R.id.profile_edit_picture_1:
                mPhoto1 = photo;
                return;
            case R.id.profile_edit_picture_2:
                mPhoto2 = photo;
                return;
            case R.id.profile_edit_picture_3:
                mPhoto3 = photo;
                return;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    void bind(User user) {
        findViewById(R.id.profile_edit_fullname, TextInputEditText.class).setText(user.namesurname);
        findViewById(R.id.profile_edit_username, TextInputEditText.class).setText(user.username);
        findViewById(R.id.profile_edit_email, TextInputEditText.class).setText(user.email);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.profile_edit_picture_1, CircleImageView.class));

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath2)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.profile_edit_picture_2, CircleImageView.class));

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath3)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.profile_edit_picture_3, CircleImageView.class));
    }

    void save() {
        if (!isValid()) {
            return;
        }

        mDisposables.add(
                Single
                        .zip(observer -> {
                            String data = getMediaData(mPhoto1);
                            if (data == null) {
                                observer.onSuccess(null);
                                return;
                            }

                            UploadImageRequest request = new UploadImageRequest(data);
                            request.token = AccountUtils.tokenLegacy(getContext());
                            request.profileImageType = ProfileImageType.PATH_1;

                            mDisposables.add(
                                    ApiManager
                                            .getInstance()
                                            .uploadImage(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<UploadImageResponse>() {
                                                @Override
                                                protected void success(UploadImageResponse response) {
                                                    observer.onSuccess(response.data);
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            }));
                        }, observer -> {
                            String data = getMediaData(mPhoto2);
                            if (data == null) {
                                observer.onSuccess(null);
                                return;
                            }

                            UploadImageRequest request = new UploadImageRequest(data);
                            request.token = AccountUtils.tokenLegacy(getContext());
                            request.profileImageType = ProfileImageType.PATH_2;

                            mDisposables.add(
                                    ApiManager
                                            .getInstance()
                                            .uploadImage(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<UploadImageResponse>() {
                                                @Override
                                                protected void success(UploadImageResponse response) {
                                                    observer.onSuccess(response.data);
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            }));
                        }, observer -> {
                            String data = getMediaData(mPhoto3);
                            if (data == null) {
                                observer.onSuccess(null);
                                return;
                            }

                            UploadImageRequest request = new UploadImageRequest(data);
                            request.token = AccountUtils.tokenLegacy(getContext());
                            request.profileImageType = ProfileImageType.PATH_3;

                            mDisposables.add(
                                    ApiManager
                                            .getInstance()
                                            .uploadImage(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<UploadImageResponse>() {
                                                @Override
                                                protected void success(UploadImageResponse response) {
                                                    observer.onSuccess(response.data);
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            }));
                        }, (Function3<String, String, String, UpdateProfileRequest>) (image1, image2, image3) -> {
                            User me = AccountUtils.me(getContext());

                            UpdateProfileRequest request = new UpdateProfileRequest();
                            request.token = AccountUtils.tokenLegacy(getContext());

                            String namesurname = findViewById(R.id.profile_edit_fullname, TextInputEditText.class).getText().toString().trim();
                            request.namesurname = StringUtils.equals(me.username, namesurname) ? null : namesurname;

                            String username = findViewById(R.id.profile_edit_username, TextInputEditText.class).getText().toString().trim();
                            request.username = StringUtils.equals(me.username, username) ? null : username;

                            String email = findViewById(R.id.profile_edit_email, TextInputEditText.class).getText().toString().trim();
                            request.email = StringUtils.equals(me.email, email) ? null : email;

                            request.imgpath1 = image1;
                            request.imgpath2 = image2;
                            request.imgpath3 = image3;
                            return request;
                        })
                        .subscribe((request, throwable) -> {
                            if (throwable != null) {
                                mView.onError(throwable);
                                return;
                            }

                            mDisposables.add(
                                    ApiManager
                                            .getInstance()
                                            .updateProfile(request)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new ServiceConsumer<UpdateProfileResponse>() {
                                                @Override
                                                protected void success(UpdateProfileResponse response) {
                                                    Logcat.v("Profile updated");

                                                    mView.onProfileUpdated();
                                                }

                                                @Override
                                                protected void error(ApiError error) {
                                                    Logcat.e(error);

                                                    mView.onError(error);
                                                }
                                            }));
                        }));
    }

    private boolean isValid() {
        String fullname = findViewById(R.id.profile_edit_fullname, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(fullname)) {
            mView.onError(new IllegalStateException("Ad Soyad boş olamaz"));
            return false;
        }

        String username = findViewById(R.id.profile_edit_username, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(username)) {
            mView.onError(new IllegalStateException("Kullanıcı adı boş olamaz"));
            return false;
        }

        String email = findViewById(R.id.profile_edit_email, TextInputEditText.class).getText().toString().trim();
        if (!RegexUtils.isEmail(email)) {
            mView.onError(new IllegalStateException("Lütfen geçerli email adresi giriniz"));
            return false;
        }

        return true;
    }
}
