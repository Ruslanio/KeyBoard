package com.example.ruslanio.keyboard.network;

import com.example.ruslanio.keyboard.network.body.AddDataRequestBody;
import com.example.ruslanio.keyboard.network.pojo.EmptyResult;
import com.example.ruslanio.keyboard.network.pojo.ServerResponce;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ruslanio on 01.12.2017.
 */

public class ApiManager {
    private static ApiManager mApiManger;
    private static final String BASE_URL = "https://text-analysis111.herokuapp.com/";

    private final ApiInterface mApiInterface;


    public static ApiManager getInstance(){
        if (mApiManger == null)
            mApiManger = new ApiManager();
        return mApiManger;
    }

    private ApiManager() {
        mApiInterface = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface.class);
    }

    public Observable<ServerResponce> getData(){
        return mApiInterface.getData();
    }

    public Call<EmptyResult> postData(AddDataRequestBody body){
        return mApiInterface.postData(body);
    }

}
