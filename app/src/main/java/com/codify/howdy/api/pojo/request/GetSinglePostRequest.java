package com.codify.howdy.api.pojo.request;

public final class GetSinglePostRequest {

    public String token;
    public long postId;

    public GetSinglePostRequest(long postId) {
        this.postId = postId;
    }
}
