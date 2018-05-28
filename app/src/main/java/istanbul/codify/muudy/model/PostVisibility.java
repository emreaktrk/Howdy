package istanbul.codify.muudy.model;

import com.google.gson.annotations.SerializedName;

public enum PostVisibility {

    @SerializedName("all") ALL("Herkes"),
    @SerializedName("friends") FRIENDS("Arkadaslar");

    private String mText;

    PostVisibility(String text) {
        mText = text;
    }

    @Override
    public String toString() {
        return mText;
    }
}
