package istanbul.codify.muudy.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.muudy.deeplink.AnswerHiLink;
import istanbul.codify.muudy.deeplink.DeepLink;

public class AnswerHiEvent extends FollowEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new AnswerHiLink(message);
    }
}
