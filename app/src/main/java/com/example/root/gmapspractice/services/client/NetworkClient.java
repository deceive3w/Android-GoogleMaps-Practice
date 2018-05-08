package com.example.root.gmapspractice.services.client;

import com.google.android.gms.common.api.Api;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    static String BASE_URL = "https://maps.googleapis.com/maps/api/";
    static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    static Retrofit retrofit = builder.build();

    public static ApiServices apiServices(){
        return retrofit.create(ApiServices.class);
    }
}
