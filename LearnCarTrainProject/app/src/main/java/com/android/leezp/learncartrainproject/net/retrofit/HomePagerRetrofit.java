package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetPublishOrderData;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface HomePagerRetrofit {
    @FormUrlEncoded
    @POST("publish_order.jsp")
    Observable<NetPublishOrderData> getPublishOrderByDriverID(@Field("role") String role, @Field("requestCode") String code, @Field("id") String driverID);

    @FormUrlEncoded
    @POST("publish_order.jsp")
    Observable<NetPublishOrderData> publishOrder(@FieldMap Map<String, String> params);
}
