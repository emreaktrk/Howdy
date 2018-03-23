package com.codify.howdy.model;

import com.google.gson.annotations.SerializedName;

public enum PostMediaType {

    @SerializedName("none") NONE,
    @SerializedName("image") IMAGE,
    @SerializedName("video") VIDEO,
}
