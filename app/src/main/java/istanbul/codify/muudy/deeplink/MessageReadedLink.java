package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.main.MainActivity;

public class MessageReadedLink extends DeepLink {

    public MessageReadedLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(MainActivity activity) {

    }
}
