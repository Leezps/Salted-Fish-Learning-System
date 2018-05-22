package com.android.leezp.learncarproject.utils.retrofit;


import com.android.leezp.learncarproject.db.RegisterDB;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface RegisterRetrofit {
    @FormUrlEncoded
    @POST("student_register.jsp")
    Observable<RegisterDB> getVerification(@Field("requestCode") String code, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("student_register.jsp")
    Observable<RegisterDB> regisetrStudent(@FieldMap Map<String, String> params);
}
