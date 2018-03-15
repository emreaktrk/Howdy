package com.codify.howdy.ui.forgotpassword;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.ForgotPasswordRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.ForgotPasswordResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordView> {

    @Override
    public void attachView(ForgotPasswordView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.forgot_password_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.forgot_password_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));
    }

    void bind(@Nullable String email) {
        findViewById(R.id.forgot_password_email, TextInputEditText.class).setText(email);
    }

    void sendEmail() {
        String email = findViewById(R.id.forgot_password_email, TextInputEditText.class).getText().toString();
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .forgotPassword(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<ForgotPasswordResponse>() {
                            @Override
                            protected void success(ForgotPasswordResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

}
