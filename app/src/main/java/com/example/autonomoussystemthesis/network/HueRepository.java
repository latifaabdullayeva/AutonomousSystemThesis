package com.example.autonomoussystemthesis.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HueRepository {
    private final Retrofit retrofit;
    private final HueService hueService;
    private final String username;

    public HueRepository(String ipAddress, String user) {
        // The Retrofit class generates an implementation of the HueService interface.
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // By default, Retrofit can only deserialize HTTP bodies into OkHttp's ResponseBody type
        // and it can only accept its RequestBody type for @Body.
        // Converters can be added to support other types. Gson: com.squareup.retrofit2:converter-gson
        // GsonConverterFactory class is needed to generate an implementation of the HueService interface
        // which uses Gson for its deserialization.
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    public void updateBrightness(int brightness) {
        HueRequest request = new HueRequest();
        request.setOn(true);
        request.setBrightness(brightness);

        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("HueRepository", "success!");
                        try {
                            Log.d("HueRepository", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("HueRepository", "failure :(", t);
                    }
                });
    }
}

