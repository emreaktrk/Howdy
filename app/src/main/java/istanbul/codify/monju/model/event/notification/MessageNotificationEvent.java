package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.MessageLink;

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
