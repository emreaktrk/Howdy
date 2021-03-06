package istanbul.codify.monju.ui.changepassword;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.ChangePasswordRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.ChangePasswordResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;

final class ChangePasswordPresenter extends BasePresenter<ChangePasswordView> {

    @Override
    public void attachView(ChangePasswordView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.change_password_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.change_password_button))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Change clicked");

                            view.onChangeClicked();
                        }));
    }

    void changePassword() {
        if (!isValid()) {
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.newpass = findViewById(R.id.change_password_new, TextInputEditText.class).getText().toString();
        request.oldpass = findViewById(R.id.change_password_current, TextInputEditText.class).getText().toString();

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .changePassword(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<ChangePasswordResponse>() {
                            @Override
                            protected void success(ChangePasswordResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    private boolean isValid() {
        return true;
    }
}
