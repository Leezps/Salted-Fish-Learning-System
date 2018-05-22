package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.DriverShowDB;
import com.android.leezp.learncarproject.db.PublishOrderDB;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface OrderDriverRetrofit {
    @FormUrlEncoded
    @POST("driver_show.jsp")
    Observable<DriverShowDB> findDriverOrderByPlace(@Field("place") String place);

    @FormUrlEncoded
    @POST("publish_order.jsp")
    Observable<PublishOrderDB> getPublishOrderByDriverID(@Field("role") String role, @Field("requestCode") String code, @Field("driverID") String driverID);
}
