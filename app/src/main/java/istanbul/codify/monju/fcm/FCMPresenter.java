package istanbul.codify.monju.fcm;

import android.content.Context;
import com.google.firebase.iid.FirebaseInstanceId;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.UpdateProfileRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.UpdateProfileResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.DeviceType;
import istanbul.codify.monju.ui.base.BasePresenter;

final class FCMPresenter extends BasePresenter<FCMView> {

    void update(Context context) {
        if (!AccountUtils.has(context)) {
            return;
        }

        String token = FirebaseInstanceId
                .getInstance()
                .getToken();

        Logcat.d("FCM : " + token);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.token = AccountUtils.tokenLegacy(context);
        request.pushToken = token;
        request.deviceType = DeviceType.ANDROID;

        mDisposables
                .add(
                        ApiManager
                                .getInstance()
                                .updateProfile(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ServiceConsumer<UpdateProfileResponse>() {
                                    @Override
                                    protected void success(UpdateProfileResponse response) {
                                        Logcat.v("Push token updated");

                                        mView.onTokenUpdated(token);
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
