package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

public enum UserType {

    @SerializedName("usercreated") USER_CREATED,
    @SerializedName("userlogined") USER_LOGINED,
}
