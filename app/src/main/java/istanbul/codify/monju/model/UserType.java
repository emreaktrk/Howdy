package istanbul.codify.monju.model;

import com.google.gson.annotations.SerializedName;

public enum UserType {

    @SerializedName("usercreated") USER_CREATED,
    @SerializedName("userlogined") USER_LOGINED,
}
