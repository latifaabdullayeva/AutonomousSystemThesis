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
// .baseUrl("http://192.168.0.102:8080/distances/")
    public DistanceRepository() {
        // Write in terminal ./ngrok http 8080 in order to ger bseURL
        // ngrok exposes local servers behind NATs and firewalls to the public internet over secure tunnels.
        // TODO: always change ngrok URL
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://1cc33072.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        distanceService = retrofit.create(DistanceService.class);
    }

    public void sendNetworkRequest(Integer fromDevice, Integer toDevice, Long distance) {
        Distance distanceRequest = new Distance(fromDevice, toDevice, distance);

        distanceService.postDistance(distanceRequest)
                .enqueue(new Callback<ResponseBody>() {

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