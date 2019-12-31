package com.example.rajat.meeting.network;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitApiClient {

    private static final String NEWS_API_URL = "https://fathomless-shelf-5846.herokuapp.com";
    private static final Object LOCK = new Object();
    private static RetrofitApi sNewsApi;
    private static RetrofitApiClient sInstance;

    public static RetrofitApi getInstance() {

        if (sInstance == null || sNewsApi == null) {
            synchronized (LOCK) {

                Interceptor networkInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // set max-age and max-stale properties for cache header
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(1, TimeUnit.HOURS)
                                .maxStale(3, TimeUnit.DAYS)
                                .build();
                        return chain.proceed(chain.request())
                                .newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", cacheControl.toString())
                                .build();
                    }
                };

                // Building OkHttp client
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(networkInterceptor)
                        .build();


                // Retrofit Builder
                Retrofit.Builder builder =
                        new Retrofit
                                .Builder()
                                .baseUrl(NEWS_API_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create(new Gson()));

                // Set NewsApi instance
                sNewsApi = builder.build().create(RetrofitApi.class);
                sInstance = new RetrofitApiClient();
            }
        }
        return sNewsApi;
    }


    // Make sure this class never initialize
    private RetrofitApiClient() {
    }
}
