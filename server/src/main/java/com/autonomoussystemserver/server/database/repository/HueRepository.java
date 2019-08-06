package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.controller.HueRequest;
import com.autonomoussystemserver.server.controller.HueService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HueRepository {

    private final HueService hueService;
    private final String username;

    public HueRepository(String ipAddress, String user) {
//         The Retrofit class generates an implementation of the HueService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//         By default, Retrofit can only deserialize HTTP bodies into OkHttp's ResponseBody type
//         and it can only accept its RequestBody type for @Body.
//         Converters can be added to support other types. Gson: com.squareup.retrofit2:converter-gson
//         GsonConverterFactory class is needed to generate an implementation of the HueService interface
//         which uses Gson for its deserialization.
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    public void updateBrightness(int brightness) {
        HueRequest request = new HueRequest(true, brightness);

        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        System.out.println("HueRepository" + " success!");
//                        try {
//                            if (response.body() != null) {
//                                System.out.println("HueRepository" + response.body());
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        System.out.println("HueRepository" + " failure : " + t);
                    }
                });
    }
}
