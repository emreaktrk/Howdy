package com.codify.howdy.api;

import com.codify.howdy.api.pojo.request.LoginRequest;
import com.codify.howdy.api.pojo.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("login")
    Single<LoginResponse> login(@Body LoginRequest request);
}
