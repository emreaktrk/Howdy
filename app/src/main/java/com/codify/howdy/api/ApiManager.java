package com.codify.howdy.api;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public final class ApiManager {

    private static ApiServices mServices;

    public static ApiServices getInstance() {
        if (mServices == null) {
            LogInterceptor logger = new LogInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);

            mServices = new Retrofit
                    .Builder()
                    .baseUrl("http://188.166.97.94:3017/api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                    .addConverterFactory(
                            GsonConverterFactory
                                    .create(
                                            new GsonBuilder()
                                                    .serializeNulls()
                                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .create()))
                    .client(
                            new OkHttpClient
                                    .Builder()
                                    .addInterceptor(logger)
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .build())
                    .build()
                    .create(ApiServices.class);
        }

        return mServices;
    }
}
