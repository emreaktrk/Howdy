package istanbul.codify.muudy.ui.forgotpassword;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.ForgotPasswordRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.ForgotPasswordResponse;
import istanbul.codify.muudy.helper.AlertDialogHelper;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Email;
import istanbul.codify.muudy.ui.base.BasePresenter;
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
                        .filter(o -> {
                            return checkFields();
                        })
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));


    }

    private boolean checkFields() {

        String email = findViewById(R.id.forgot_password_email, TextInputEditText.class).getText().toString().trim();
        if (email.length() == 0) {
            AlertDialogHelper.showAlert("Email boş olamaz.",mRoot.getContext());
            return false;
        }else{
            if (!new Email(email).isValid()){
                AlertDialogHelper.showAlert("Girilen Email adresi geçersiz.",mRoot.getContext());
                return false;
            }else{
                return  true;
            }
        }
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
