package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetDriverData;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginOrRegisterRetrofit {
    @FormUrlEncoded
    @POST("driver_login.jsp")
    Observable<NetDriverData> loginDriver(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("driver_register.jsp")
    Observable<NetDriverData> registerGetVerification(@Field("requestCode") String code, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("driver_register.jsp")
    Observable<NetDriverData> registerDriver(@FieldMap Map<String, String> params);
}
