package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;

public final class MessageNotificationEvent extends NotificationEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return null;
    }
}