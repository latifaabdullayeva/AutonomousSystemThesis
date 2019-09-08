package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.InteractionTimesDto;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import com.autonomoussystemserver.server.service.InteractionTimesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HueRepository {

    private final HueService hueService;
    private final String username;

    @Autowired
    InteractionTimesRepository interactionTimesRepository;

    @Autowired
    InteractionTimesDto interactionTimesDto;

    @Autowired
    InteractionTimesService interactionTimesService;


    public HueRepository(String ipAddress, String user) {
        System.out.println("HueRepository constructor");
//         The Retrofit class generates an implementation of the HueService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    public void updateBrightness(int brightness, int hue, int saturation) {
        System.out.println("HueRepository updateBrightness");
        HueRequest request = new HueRequest(true, brightness, hue, saturation);

        // InteractionTimes interactionTimes = interactionTimesService.create(interactionTimesDto);


        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Response<okhttp3.ResponseBody> response) {
                        System.out.println("HueRepository " + "success!");
                        try {
                            if (response.body() != null) {
                                System.out.println("HueRepository " + response.body().string());
                                System.out.println("HueRepository ------------------------------------------------------------------------------------------------------------------------------");
                                // TODO: we get interactionTimes that is mascot=ourMascot
                                //  and make POST (interactionTimes + 1)

                                interactionTimesService.incrementInteractionTimes(interactionTimesDto);
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