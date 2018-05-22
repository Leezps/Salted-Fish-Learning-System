package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.HelpDB;

import retrofit2.http.GET;
import rx.Observable;

public interface HelpRetrofit {
    @GET("help.jsp?role=0")
    Observable<HelpDB> getHelp();
}
