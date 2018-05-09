package istanbul.codify.muudy.model;

import android.support.annotation.IntRange;

public final class NotificationSettings {

    public @IntRange(from = 0, to = 1) int onLike;
    public @IntRange(from = 0, to = 1) int onFollow;
    public @IntRange(from = 0, to = 1) int onTag;
}
