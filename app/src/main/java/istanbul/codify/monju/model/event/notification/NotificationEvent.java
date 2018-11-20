package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.deeplink.DeepLink;

public abstract class NotificationEvent {

    public @Nullable RemoteMessage message;

    public abstract @Nullable DeepLink getDeepLink();
}
