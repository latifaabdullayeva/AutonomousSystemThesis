package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.controller.HueRequest;
import com.autonomoussystemserver.server.controller.HueService;
import org.springframework.lang.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class HueRepository {

    private final HueService hueService;
    private final String username;

    public HueRepository(String ipAddress, String user) {
        System.out.println("Backend: " + "HueRepository constructor");
//         The Retrofit class generates an implementation of the HueService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    public void updateBrightness(int brightness) {
        System.out.println("Backend: " + "HueRepository updateBrightness");
        HueRequest request = new HueRequest(true, brightness);

        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Response<okhttp3.ResponseBody> response) {
                        System.out.println("HueRepository " + "success!");
                        try {
                            if (response.body() != null) {
                                System.out.println("HueRepository " + response.body().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
                        System.out.println("HueRepository " + " failure :( " + t);
                    }
                });
    }
}