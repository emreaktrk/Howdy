package istanbul.codify.muudy.ui.profileedit;

import android.Manifest;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.List;

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

    void save() {
        if (!isValid()) {
            mView.onError(new IllegalStateException("Lutfen alanlari doldurunuz"));
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.namesurname = findViewById(R.id.profile_edit_fullname, TextInputEditText.class).getText().toString().trim();
        request.username = findViewById(R.id.profile_edit_username, TextInputEditText.class).getText().toString().trim();
        request.email = findViewById(R.id.profile_edit_email, TextInputEditText.class).getText().toString().trim();

        mDisposables
                .add(
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
                                })
                );
    }

    private boolean isValid() {
        String fullname = findViewById(R.id.profile_edit_fullname, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(fullname)) {
            return false;
        }

        String username = findViewById(R.id.profile_edit_username, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(username)) {
            return false;
        }

        String email = findViewById(R.id.profile_edit_email, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(email)) {
            return false;
        }

        return true;
    }
}
