package com.example.mymascotapp.network.api.distance;

import android.support.annotation.NonNull;
import android.util.Log;

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

    public DistanceRepository() {
        Log.d("FLOW", "DistanceRepository");

//        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl("https://0f608cb3.ngrok.io")
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build();

        distanceService = retrofit.create(DistanceService.class);
    }

    public void sendNetworkRequest(Integer fromDevice, Integer toDevice, Long distance) {
        Log.d(TAG, "sendNetworkRequest()");
        Distance distanceRequest = new Distance(fromDevice, toDevice, distance);

        distanceService.postDistance(distanceRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! " + response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "failure :(", t);
            }
        });
    }

    public void getNetworkRequest(Callback<ApiDistanceResponse> callback) {
        Log.d(TAG, "getNetworkRequest() callback = " + callback);
        distanceService.getDistances().enqueue(callback);

    }
}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL