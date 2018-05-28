package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Activity;

public class MessageReadedLink extends DeepLink {

    public MessageReadedLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {

    }
}
