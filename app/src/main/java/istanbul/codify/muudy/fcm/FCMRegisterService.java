package istanbul.codify.muudy.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

public final class FCMRegisterService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        UpdatePushService.start();
    }
}