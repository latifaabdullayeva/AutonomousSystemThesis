package com.example.mytabletapp.api.distance;

import android.util.Log;

import com.example.mytabletapp.api.devices.Device;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DistanceRepository {
    protected static final String TAG = "DistanceRepository";
    private final DistanceService distanceService;

    public DistanceRepository(String address) {
        Log.d("FLOW", "DistanceRepository");

        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl(address)
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build();

        distanceService = retrofit.create(DistanceService.class);
    }

    public void sendNetworkRequest(Integer id, Device fromDevice, Device toDevice, Long distance) {
        Log.d(TAG, "sendNetworkRequest()");
        Distance distanceRequest = new Distance(null,fromDevice, toDevice, distance);

        distanceService.postDistance(distanceRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse( Call<ResponseBody> call,  Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! " + response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( Call<ResponseBody> call,  Throwable t) {
                Log.e(TAG, "failure :(", t);
            }
        });
    }

    public void getNetworkRequest(Callback<ApiDistanceResponse> callback) {
        Log.d(TAG, "getNetworkRequest() callback = " + callback);
        distanceService.getDistances().enqueue(callback);

    }
}