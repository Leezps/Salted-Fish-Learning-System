package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetBookSuccessOrderData;
import com.android.leezp.learncartrainproject.net.data.NetRelationBookSuccessOrderData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface BookSuccessOrderRetrofit {
    @FormUrlEncoded
    @POST("complete_order.jsp")
    Observable<NetBookSuccessOrderData> getOrRemoveBookSuccessOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("relation_complete_order.jsp")
    Observable<NetRelationBookSuccessOrderData> getRelationBookSuccessOrder(@FieldMap Map<String, String> params);
}
