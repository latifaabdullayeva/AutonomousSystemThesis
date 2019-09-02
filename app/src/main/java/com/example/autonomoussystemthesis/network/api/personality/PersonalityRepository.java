package com.example.autonomoussystemthesis.network.api.personality;

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

public class PersonalityRepository {
    protected static final String TAG = "PersonalityRepository";

    private final PersonalityService personalityService;

    public PersonalityRepository() {
        Log.d("FLOW", "PersonalityRepository");

//        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build();

        personalityService = retrofit.create(PersonalityService.class);
        Log.d(TAG, "PersonalityRepo");
    }

    public void sendNetworkRequestPers(Integer id, String personality_name, String hue_color, Integer bri, Integer hue, Integer sat, String screen_color, Integer vibration_level, String music_genre) {
        Personality personalityRequest = new Personality(null, personality_name, hue_color, bri, hue, sat, screen_color, vibration_level, music_genre);
        Log.d(TAG, "Personality sendNetReq");

        personalityService.createPersonality(personalityRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "Response: " + response.body());
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! \n"
                                + response.body().string());
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

    public void getNetworkRequest(Callback<ApiPersonalityResponse> callback) {
        personalityService.getPersonality().enqueue(callback);
        Log.d(TAG, "Personality getNetworkRequest");
    }
}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL