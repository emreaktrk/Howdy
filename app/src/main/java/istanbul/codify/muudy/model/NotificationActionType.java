package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

public enum NotificationActionType {

    @SerializedName("hello") HELLO,
    @SerializedName("like") LIKE,
    @SerializedName("follow") FOLLOW,
}
