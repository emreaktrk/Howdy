package istanbul.codify.monju.api.pojo.request;

import android.support.annotation.IntRange;
import istanbul.codify.monju.model.NotificationSettings;

public final class UpdatePushSettingsRequest {

    public String token;
    public @IntRange(from = 0, to = 1) int onLike;
    public @IntRange(from = 0, to = 1) int onFollow;
    public @IntRange(from = 0, to = 1) int onTag;

    public UpdatePushSettingsRequest(NotificationSettings settings) {
        onFollow = settings.onFollow;
        onLike = settings.onLike;
        onTag = settings.onTag;
    }
}
