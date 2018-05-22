package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.BaiduMapDB;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BaiduMapRetrofit {
    @GET("geocoder")
    Observable<BaiduMapDB> getPlaceByLatAndLng(@Query("output") String output, @Query("location") String location);
}
