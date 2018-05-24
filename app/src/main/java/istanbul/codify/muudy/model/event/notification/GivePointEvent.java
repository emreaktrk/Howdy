package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.GivePointLink;

public class GivePointEvent extends NotificationEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new GivePointLink(message);
    }
}
