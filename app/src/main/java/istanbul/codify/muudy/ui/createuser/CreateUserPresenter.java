package istanbul.codify.muudy.ui.createuser;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.UpdateProfileRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.UpdateProfileResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class CreateUserPresenter extends BasePresenter<CreateUserView> {

    @Override
    public void attachView(CreateUserView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.create_user_create))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Create user clicked");

                            view.onCreateClicked();
                        }));
    }

    void createUser(@NonNull User user) {
        String username = findViewById(R.id.create_user_username, TextInputEditText.class).getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            IllegalStateException exception = new IllegalStateException("Kullanıcı adı girmediniz");
            mView.onError(exception);
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.token = user.tokenstring;
        request.username = username;

        mDisposables
                .add(
                        ApiManager
                                .getInstance()
                                .updateProfile(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ServiceConsumer<UpdateProfileResponse>() {
                                    @Override
                                    protected void success(UpdateProfileResponse response) {
                                        Logcat.v("User created");

                                        user.username = findViewById(R.id.create_user_username, TextInputEditText.class).getText().toString().trim();
                                        mView.onLoaded(user);
                                    }

                                    @Override
                                    protected void error(ApiError error) {
                                        Logcat.e(error);

                                        mView.onError(error);
                                    }
                                })
                );
    }
}
