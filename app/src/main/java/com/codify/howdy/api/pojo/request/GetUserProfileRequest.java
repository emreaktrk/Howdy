package com.codify.howdy.api.pojo.request;

import android.support.annotation.NonNull;

public final class GetUserProfileRequest {

    public String token;
    public long id;

    public GetUserProfileRequest(@NonNull Long userId) {
        this.id = userId;
    }
}
