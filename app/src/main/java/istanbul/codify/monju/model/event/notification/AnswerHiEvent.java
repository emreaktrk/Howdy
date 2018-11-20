package istanbul.codify.monju.model.event.notification;

import android.support.annotation.Nullable;
import istanbul.codify.monju.deeplink.AnswerHiLink;
import istanbul.codify.monju.deeplink.DeepLink;

public class AnswerHiEvent extends FollowEvent {
    @Nullable
    @Override
    public DeepLink getDeepLink() {
        return new AnswerHiLink(message);
    }
}
