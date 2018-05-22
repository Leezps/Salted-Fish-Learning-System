package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetMessageCenterData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MessageCenterRetrofit {
    @FormUrlEncoded
    @POST("message_center.jsp")
    Observable<NetMessageCenterData> getOrUpdateOrRemoveMessage(@FieldMap Map<String, String> params);
}
