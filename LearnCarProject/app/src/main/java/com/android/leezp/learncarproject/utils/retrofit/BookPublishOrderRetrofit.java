package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.PublishOrderDB;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface BookPublishOrderRetrofit {
    @FormUrlEncoded
    @POST("publish_order.jsp")
    Observable<PublishOrderDB> studentBookOrder(@FieldMap Map<String, String> params);
}
