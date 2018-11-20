package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.MessageReadedLink;

public class MessageReadedNotificationEvent extends NotificationEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new MessageReadedLink(message);
    }


}
