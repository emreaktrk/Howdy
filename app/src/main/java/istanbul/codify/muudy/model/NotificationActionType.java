package istanbul.codify.muudy.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import istanbul.codify.muudy.model.event.notification.*;

public enum NotificationActionType {

    @SerializedName("none") NONE,
    @SerializedName("like") LIKE,
    @SerializedName("comment") COMMENT,
    @SerializedName("message") MESSAGE,
    @SerializedName("follow") FOLLOW,
    @SerializedName("followrequest") FOLLOW_REQUEST,
    @SerializedName("weeklyTopUsers") WEEK_TOP_USERS,
    @SerializedName("post") POST,
    @SerializedName("sayhi") SAY_HI,
    @SerializedName("answerhi") ANSWER_HI,
    @SerializedName("givevote") GIVE_VOTE,
    @SerializedName("tag") TAG,
    @SerializedName("generalAnnounce") GENERAL_ANNOUNCE,
    @SerializedName("activityReminder") ACTIVITY_REMINDER,
    @SerializedName("messagereaded") MESSAGE_READED;

    public @Nullable
    static NotificationActionType value(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new Gson().fromJson(value, NotificationActionType.class);
    }

    public NotificationEvent getEvent() {
        switch (this) {
            case MESSAGE:
                return new MessageNotificationEvent();
            case SAY_HI:
                return new SayHiNotificationEvent();
            case MESSAGE_READED:
                return new MessageReadedNotificationEvent();
            case COMMENT:
                return new PostDetailEvent();
            case POST:
                return new PostDetailEvent();
            case LIKE:
                return new PostDetailEvent();
            case TAG:
                return new PostDetailEvent();
            case GENERAL_ANNOUNCE:
                return new GeneralNotificationEvent();
            case FOLLOW_REQUEST:
                return new GeneralNotificationEvent();
            case FOLLOW:
                return new FollowEvent();
            case ACTIVITY_REMINDER:
                //TODO
                return null;
            case ANSWER_HI:
                //TODO
                return null;
            case WEEK_TOP_USERS:
                //TODO
                return null;
            case GIVE_VOTE:
                //TODO
                return null;
            case NONE:
                //TODO
                return null;
            default:
                return null;
        }
    }
}
