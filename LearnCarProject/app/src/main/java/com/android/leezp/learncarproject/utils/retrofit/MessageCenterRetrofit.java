package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.MessageCenterDB;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MessageCenterRetrofit {
    @FormUrlEncoded
    @POST("message_center.jsp")
    Observable<MessageCenterDB> getMessageCenter(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("message_center.jsp")
    Observable<MessageCenterDB> removeOrUpdateMessage(@FieldMap Map<String, String> params);
}
