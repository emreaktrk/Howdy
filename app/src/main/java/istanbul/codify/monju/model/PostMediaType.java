package istanbul.codify.monju.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public enum PostMediaType {

    @SerializedName("none") NONE,
    @SerializedName("image") IMAGE,
    @SerializedName("video") VIDEO,
    @Expose RECOMMENDED_USERS,
}
