package com.example.ruslanio.keyboard.network;

import com.example.ruslanio.keyboard.network.body.AddDataRequestBody;
import com.example.ruslanio.keyboard.network.pojo.EmptyResult;
import com.example.ruslanio.keyboard.network.pojo.ServerResponce;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ruslanio on 30.11.2017.
 */

public interface ApiInterface {

    @POST("sentiment/send")
    Call<Void> postData(@Body AddDataRequestBody body);

    @POST("sentiment/send")
    Call<Void> postData(@Query("text") String text);

    @GET("records")
    Observable<ServerResponce> getData();
}
