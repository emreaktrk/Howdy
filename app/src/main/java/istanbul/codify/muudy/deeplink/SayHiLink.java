package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Activity;
import istanbul.codify.muudy.ui.response.ResponseActivity;

public final class SayHiLink extends DeepLink {

    public SayHiLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        ResponseActivity.start(getItemId(),getNotificationMessage());

    }
}
