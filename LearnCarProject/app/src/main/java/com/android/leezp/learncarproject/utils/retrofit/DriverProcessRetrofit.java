package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.DriverProcessDB;

import retrofit2.http.GET;
import rx.Observable;

public interface DriverProcessRetrofit {
    @GET("student_driver_process.jsp")
    Observable<DriverProcessDB> requestDriverProcess();
}
