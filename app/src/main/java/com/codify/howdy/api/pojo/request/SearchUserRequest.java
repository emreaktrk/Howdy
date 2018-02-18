package com.codify.howdy.api.pojo.request;


public final class SearchUserRequest {

    public String token;
    public String text;

    public SearchUserRequest(String text) {
        this.text = text;
    }
}
