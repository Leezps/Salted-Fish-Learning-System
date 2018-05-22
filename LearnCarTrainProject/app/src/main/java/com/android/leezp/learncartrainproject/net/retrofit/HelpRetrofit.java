package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetHelpData;

import retrofit2.http.GET;
import rx.Observable;

public interface HelpRetrofit {
    @GET("help.jsp?role=1")
    Observable<NetHelpData> getApplicationHelp();
}
