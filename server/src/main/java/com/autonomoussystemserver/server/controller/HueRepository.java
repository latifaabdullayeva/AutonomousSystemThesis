package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.InteractionTimesDto;
import com.autonomoussystemserver.server.database.repository.InteractionTimesRepository;
import com.autonomoussystemserver.server.service.InteractionTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HueRepository {

    private final HueService hueService;
    private final String username;

    @Autowired
    InteractionTimesRepository interactionTimesRepository;

    @Autowired
    private
    InteractionTimesDto interactionTimesDto;

    @Autowired
    private
    InteractionTimesService interactionTimesService;


    HueRepository(String ipAddress, String user) {
//         The Retrofit class generates an implementation of the HueService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    void updateBrightness(boolean lampState, int brightness, int hue, int saturation) {
        HueRequest request = new HueRequest(lampState, brightness, hue, saturation);

        // InteractionTimes interactionTimes = interactionTimesService.create(interactionTimesDto);


        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Response<okhttp3.ResponseBody> response) {
                        if (response.body() != null) {
                            interactionTimesService.incrementInteractionTimes(interactionTimesDto);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
                    }
                });
    }
}