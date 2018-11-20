package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.GivePointLink;

public class GivePointEvent extends ProfileEvent {

    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new GivePointLink(message);
    }
}
