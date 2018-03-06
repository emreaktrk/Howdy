package com.codify.howdy.model;

import com.google.gson.annotations.SerializedName;

public enum NotificationType {

    @SerializedName("me") ME,
    @SerializedName("followings") FOLLOWINGS,
}
