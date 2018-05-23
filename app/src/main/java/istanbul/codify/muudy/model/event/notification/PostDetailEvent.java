package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.PostDetailLink;

public class PostDetailEvent extends NotificationEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new PostDetailLink(message);
    }
}
