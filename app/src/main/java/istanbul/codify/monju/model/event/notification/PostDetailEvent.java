package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.DeepLink;
import istanbul.codify.monju.deeplink.PostDetailLink;

public class PostDetailEvent extends ProfileEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new PostDetailLink(message);
    }
}
