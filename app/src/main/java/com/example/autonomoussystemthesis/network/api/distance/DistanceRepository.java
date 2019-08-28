package com.example.autonomoussystemthesis.network.api.distance;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
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

        Retrofit retrofit;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        distanceService = retrofit.create(DistanceService.class);
    }

    public void sendNetworkRequest(Integer fromDevice, Integer toDevice, Long distance) {
        Distance distanceRequest = new Distance(fromDevice, toDevice, distance);

        distanceService.postDistance(distanceRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "Response: " + response.body());
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! \n" + response.body().string());
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
}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL