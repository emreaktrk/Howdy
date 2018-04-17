package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

public enum Gender implements Serializable {

    @SerializedName("male") MALE,
    @SerializedName("female") FEMALE;

    public static Gender valueOf(CharSequence value) {
        return valueOf(value);
    }

    @Override
    public String toString() {
        return (this + "").toLowerCase(Locale.ENGLISH);
    }
}
