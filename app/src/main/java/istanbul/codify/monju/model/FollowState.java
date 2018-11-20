package istanbul.codify.monju.model;

import com.google.gson.annotations.SerializedName;

public enum FollowState {

    @SerializedName("0") NOT_FOLLOWING,
    @SerializedName("1") FOLLOWING,
    @SerializedName("2") REQUEST_SENT,
}
