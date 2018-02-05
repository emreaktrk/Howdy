package com.codify.howdy.api;

import com.codify.howdy.api.pojo.request.LoginRequest;
import com.codify.howdy.api.pojo.request.RegisterRequest;
import com.codify.howdy.api.pojo.response.GetWallRequest;
import com.codify.howdy.api.pojo.response.GetWallResponse;
import com.codify.howdy.api.pojo.response.GetWordsResponse;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterRequest;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterResponse;
import com.codify.howdy.api.pojo.response.LoginResponse;
import com.codify.howdy.api.pojo.response.RegisterResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("login")
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Single<RegisterResponse> register(@Body RegisterRequest request);

    @POST("getwall")
    Single<GetWallResponse> getWall(@Body GetWallRequest request);

    @POST("getwords")
    Single<GetWordsResponse> getWords();

    @POST("getwordswithfilter")
    Single<GetWordsWithFilterResponse> getWordsWithFilter(@Body GetWordsWithFilterRequest request);
}