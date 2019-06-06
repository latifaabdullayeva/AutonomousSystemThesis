package com.example.autonomoussystemthesis.network.api.distance;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DistanceRepository {
    private final Retrofit retrofit;
    private final DistanceService distanceService;

    public DistanceRepository() {
        // Write in terminal ./ngrok http 8080 in order to ger bseURL
        // ngrok exposes local servers behind NATs and firewalls to the public internet over secure tunnels.
        // TODO: always change ngrok URL
        retrofit = new Retrofit.Builder()
                .baseUrl("http://75f14d6f.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        distanceService = retrofit.create(DistanceService.class);
    }

    public void sendNetworkRequest(Integer from, Integer to, double distance) {
        Distance distanceRequest = new Distance(from, to, distance);

        distanceService.postDistance(distanceRequest)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("DistanceRepository", "success!");
                        try {
                            Log.d("DistanceRepository", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("DistanceRepository", "failure :(", t);
                    }
                });
    }
}