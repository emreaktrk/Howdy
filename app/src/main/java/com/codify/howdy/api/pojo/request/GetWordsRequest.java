package com.codify.howdy.api.pojo.request;


public final class GetWordsRequest {

    public long categoryID;

    public GetWordsRequest(long categoryID) {
        this.categoryID = categoryID;
    }
}
