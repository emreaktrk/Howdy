package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

public enum Visibility {

    @SerializedName("all") ALL,
    @SerializedName("0") VISIBLE,
    @SerializedName("1") HIDDEN,
}
