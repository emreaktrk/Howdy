package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

public enum ReportType {

    @SerializedName("message") MESSAGE,
    @SerializedName("post") POST,
    @SerializedName("comment") COMMENT,
    @SerializedName("user") USER,
}
