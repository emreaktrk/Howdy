package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.WeeklyTopLink;

public class WeeklyTopEvent extends ProfileEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new WeeklyTopLink(message);
    }
}
