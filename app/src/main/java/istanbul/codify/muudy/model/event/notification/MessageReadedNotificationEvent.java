package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.MessageReadedLink;

public class MessageReadedNotificationEvent extends NotificationEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new MessageReadedLink(message);
    }


}
