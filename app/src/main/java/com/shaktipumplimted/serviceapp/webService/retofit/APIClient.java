package com.shaktipumplimted.serviceapp.webService.retofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.webService.api.APIS;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static Retrofit retrofit = null,retrofit2 = null,retrofit3 = null;
   public static Context mContext;
    public static Retrofit getRetrofit(Context context) {
        mContext = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);
                /*.addInterceptor(chain -> {
                    Request request=chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + Utility.getSharedPreferences(mContext, Constant.accessToken))
                            .build();
                    return chain.proceed(request);
                })*/;; //add logging interceptor as the last interceptor,
        // because this shall also show other interceptors
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIS.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitDirection(Context context) {
        mContext = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request=chain.request().newBuilder()
                            .build();
                    return chain.proceed(request);
                });; //add logging interceptor as the last interceptor,
        // because this shall also show other interceptors
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(APIS.DIRECTIONBASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit2;
    }

    public static Retrofit getRetrofitOTP(Context context) {
        mContext = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request=chain.request().newBuilder()
                            .build();
                    return chain.proceed(request);
                });; //add logging interceptor as the last interceptor,
        // because this shall also show other interceptors
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit3 == null) {
            retrofit3 = new Retrofit.Builder()
                    .baseUrl(APIS.OTPBASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit3;
    }


}
