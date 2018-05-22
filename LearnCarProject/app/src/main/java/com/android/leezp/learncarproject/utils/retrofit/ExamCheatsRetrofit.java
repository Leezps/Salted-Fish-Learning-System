package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.ExamCheatsDB;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ExamCheatsRetrofit {
    @GET("student_exam_cheats.jsp")
    Observable<ExamCheatsDB> requestExamCheats(@Query("process_order") String processOrder);
}
