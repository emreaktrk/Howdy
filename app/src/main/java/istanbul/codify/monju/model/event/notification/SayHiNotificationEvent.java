package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.SayHiLink;

public final class SayHiNotificationEvent extends ProfileEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new SayHiLink(message);
    }
}
