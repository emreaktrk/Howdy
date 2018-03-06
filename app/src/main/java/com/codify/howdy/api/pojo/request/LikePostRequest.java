package com.codify.howdy.api.pojo.request;

public final class LikePostRequest {

    public String token;
    public long postId;

    public LikePostRequest(long postId) {
        this.postId = postId;
    }
}
