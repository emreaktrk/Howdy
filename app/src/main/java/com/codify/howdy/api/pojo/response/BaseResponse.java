package com.codify.howdy.api.pojo.response;

import com.google.gson.annotations.Expose;

public abstract class BaseResponse<T> {

    @Expose
    public T data;

    @Expose
    public String err;

    @Expose
    public String errMes;

    public BaseResponse() {
    }
}
