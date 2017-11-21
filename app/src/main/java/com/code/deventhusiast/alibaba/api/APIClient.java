package com.code.deventhusiast.alibaba.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //    public static final String BASE_URL = "http://192.168.137.1:8080/kassouaApp/public/api/";
    public static final String BASE_URL = "http://kassoua.digitekplus.com/api/";
    //    public static final String BASE_URL = "http://192.168.1.2:8080/kassouaApp/public/api/";
//    public static final String PHOTO_BASE_URL = "http://192.168.1.2:8080/kassouaApp/storage/app/";
//    public static final String PHOTO_BASE_URL = "http://192.168.137.1:8080/kassouaApp/storage/app/";
    public static final String PHOTO_BASE_URL = "http://kassoua.digitekplus.com/storage/app/";

    //public static final String BASE_URL = "http://172.30.91.134:8080/kassouaApp/public/api/";
    public static final String PUT_METHOD = "PUT";
    private static Retrofit retrofit = null;


    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
