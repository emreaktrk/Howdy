package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.MessageLink;

public final class MessageNotificationEvent extends NotificationEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new MessageLink(message);
    }

    public Long getUserId() {
        if (message != null) {
            return Long.parseLong(message.getData().get("itemid"));
        }

        return null;
    }
}
