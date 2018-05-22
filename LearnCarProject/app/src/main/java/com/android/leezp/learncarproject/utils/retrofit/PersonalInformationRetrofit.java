package com.android.leezp.learncarproject.utils.retrofit;

import com.android.leezp.learncarproject.db.PersonalInformationDB;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

public interface PersonalInformationRetrofit {
    @Multipart
    @POST("student_image_upload.jsp")
    Observable<PersonalInformationDB> uploadImage(@PartMap Map<String, String> map, @Part("file\";filename=\"image.png\"")RequestBody image);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("student_information_upload.jsp")
    Observable<PersonalInformationDB> uploadPersonalInformation(@Body RequestBody information);
}
