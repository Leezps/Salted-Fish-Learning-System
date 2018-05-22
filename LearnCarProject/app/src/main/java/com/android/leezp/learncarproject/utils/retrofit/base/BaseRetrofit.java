package com.android.leezp.learncarproject.utils.retrofit.base;


import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {
    private OkHttpClient client = new OkHttpClient();
    private volatile static BaseRetrofit instance = null;
    private Retrofit retrofit = null;

    public static BaseRetrofit getInstance() {
        if (instance == null) {
            synchronized (BaseRetrofit.class) {
                if (instance == null) {
                    instance = new BaseRetrofit();
                }
            }
        }
        return instance;
    }

    public BaseRetrofit() {
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ActivityConstantEntity.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Object getRetrofit(Class className) {
        return retrofit.create(className);
    }
}
