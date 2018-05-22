package com.android.leezp.learncartrainproject.net.retrofit;

import com.android.leezp.learncartrainproject.net.data.NetDriverData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

public interface TrainInformationRetrofit {
    @Multipart
    @POST("driver_image_upload.jsp")
    Observable<NetDriverData> uploadOrRenewImage(@PartMap Map<String, String> params, @Part("file\";filename=\"image.png\"") RequestBody image);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("driver_infor_upload.jsp")
    Observable<NetDriverData> uploadOrRenewTrainerInformation(@Body RequestBody information);
}
