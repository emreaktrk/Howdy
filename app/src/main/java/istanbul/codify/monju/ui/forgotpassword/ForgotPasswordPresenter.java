package istanbul.codify.monju.ui.forgotpassword;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.ForgotPasswordRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.ForgotPasswordResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;

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
                        .filter(o -> isValid())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));


    }

    private boolean isValid() {
        String email = findViewById(R.id.forgot_password_email, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(email)) {
            new AlertDialog
                    .Builder(getContext())
                    .setMessage("Email boş olamaz.")
                    .setCancelable(true)
                    .setPositiveButton("Tamam",
                            (dialog, id) -> dialog.cancel())
                    .create()
                    .show();

            return false;
        }

        if (!RegexUtils.isEmail(email)) {
            new AlertDialog
                    .Builder(getContext())
                    .setMessage("Girilen Email adresi geçersiz.")
                    .setCancelable(true)
                    .setPositiveButton("Tamam",
                            (dialog, id) -> dialog.cancel())
                    .create()
                    .show();

            return false;
        }

        return true;
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
