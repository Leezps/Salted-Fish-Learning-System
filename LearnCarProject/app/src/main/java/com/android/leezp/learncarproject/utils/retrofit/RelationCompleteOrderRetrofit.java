package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.RelationCompleteOrderDB;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface RelationCompleteOrderRetrofit {
    @FormUrlEncoded
    @POST("relation_complete_order.jsp")
    Observable<RelationCompleteOrderDB> getRelationCompleteOrder(@Field("role") String role, @Field("orderID") String orderID, @Field("driverID") String driverID, @Field("studentID") String studentID);
}
