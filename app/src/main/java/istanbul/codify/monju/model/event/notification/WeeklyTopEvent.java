package istanbul.codify.monju.model.event.notification;

import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.WeeklyTopLink;

public final class WeeklyTopEvent extends ProfileEvent {

    @Override
    public DeepLink getDeepLink() {
        return new WeeklyTopLink(message);
    }
}
