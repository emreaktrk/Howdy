package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("activityReminder") ACTIVITY_REMINDER,
}
