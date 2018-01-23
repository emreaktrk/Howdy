package com.codify.howdy.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiManager {

    private static ApiServices mServices;

    public static ApiServices getInstance() {
        if (mServices == null) {
            mServices = new Retrofit
                    .Builder()
                    .baseUrl("http://188.166.97.94:3017/api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                    .addConverterFactory(
                            GsonConverterFactory
                                    .create(
                                            new GsonBuilder()
                                                    .serializeNulls()
                                                    .create()))
                    .build()
                    .create(ApiServices.class);
        }

        return mServices;
    }
}
