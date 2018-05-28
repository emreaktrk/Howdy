package istanbul.codify.muudy.model.event.notification;

import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.WeeklyTopLink;

public final class WeeklyTopEvent extends ProfileEvent {

    @Override
    public DeepLink getDeepLink() {
        return new WeeklyTopLink(message);
    }
}
