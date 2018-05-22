package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.CompleteOrderDB;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface CompleteOrderRetrofit {
    @FormUrlEncoded
    @POST("complete_order.jsp")
    Observable<CompleteOrderDB> getCompleteOrders(@Field("code") String requestCode, @Field("role") String role, @Field("id") String id, @Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("complete_order.jsp")
    Observable<CompleteOrderDB> requestRemoveCompleteOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("complete_order.jsp")
    Observable<CompleteOrderDB> getCompOrderByPublishOrder(@Field("code") String requestCode, @Field("publishOrderId") String publishOrderId);
}
