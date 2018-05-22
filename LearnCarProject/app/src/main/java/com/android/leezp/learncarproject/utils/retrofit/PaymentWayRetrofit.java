package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.PaymentWayDB;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface PaymentWayRetrofit {
    @FormUrlEncoded
    @POST("student_get_payment.jsp")
    Observable<PaymentWayDB> operationPaymentWay(@FieldMap Map<String, String> params);
}
