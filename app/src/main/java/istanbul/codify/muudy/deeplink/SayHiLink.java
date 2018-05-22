package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.main.MainActivity;

public final class SayHiLink extends DeepLink {

    public SayHiLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(MainActivity activity) {
        // TODO Get user from remote message
        // TODO Start Response activity
    }
}
