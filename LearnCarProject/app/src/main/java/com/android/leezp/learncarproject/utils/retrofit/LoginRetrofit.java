package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.LoginDB;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginRetrofit {
    @FormUrlEncoded
    @POST("student_login.jsp")
    Observable<LoginDB> getStudentLogin(@Field("phone") String phone, @Field("password") String password);
}
