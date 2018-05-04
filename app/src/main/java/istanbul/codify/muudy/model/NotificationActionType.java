package istanbul.codify.muudy.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import istanbul.codify.muudy.model.event.notification.MessageNotificationEvent;
import istanbul.codify.muudy.model.event.notification.NotificationEvent;
import istanbul.codify.muudy.model.event.notification.SayHiNotificationEvent;

public enum NotificationActionType {

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
    @SerializedName("activityReminder") ACTIVITY_REMINDER;

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
            default:
                return null;
        }
    }
}
