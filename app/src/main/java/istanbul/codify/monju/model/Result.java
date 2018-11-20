package istanbul.codify.monju.model;

import com.google.gson.annotations.SerializedName;

public enum Result {

    @SerializedName("ok") OK,
    @SerializedName("1") TRUE,
    @SerializedName("0") FALSE,
}
