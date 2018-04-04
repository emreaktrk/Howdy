package com.codify.howdy.api.pojo.request;

public final class GetFollowedUsersRequest {

    public String token;
    public long userId;

    public GetFollowedUsersRequest() {
    }

    public GetFollowedUsersRequest(long userId) {
        this.userId = userId;
    }
}
