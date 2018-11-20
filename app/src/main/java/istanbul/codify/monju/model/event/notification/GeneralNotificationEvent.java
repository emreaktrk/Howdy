package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.GeneralNotificationLink;

public class GeneralNotificationEvent extends ProfileEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new GeneralNotificationLink(message);

    }
}
