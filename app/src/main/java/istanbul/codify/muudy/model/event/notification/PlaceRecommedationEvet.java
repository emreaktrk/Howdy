package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;

import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.PlaceRecommedationLink;

/**
 * Created by egesert on 22.06.2018.
 */

public class PlaceRecommedationEvet extends NotificationEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new PlaceRecommedationLink(message);
    }
}
