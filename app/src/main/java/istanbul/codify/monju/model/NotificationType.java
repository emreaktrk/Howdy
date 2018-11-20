package istanbul.codify.monju.model;

import com.google.gson.annotations.SerializedName;

public enum NotificationType {

    @SerializedName("me") ME,
    @SerializedName("followings") FOLLOWINGS,
}
