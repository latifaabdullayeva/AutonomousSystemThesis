package com.example.mytabletapp.api.interaction;

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

public class InteractionRepository {
    protected static final String TAG = "InteractionRepository";
    private final InteractionService interactionService;

    public InteractionRepository(String address) {
        Log.d("FLOW", "InteractionRepository");

//      retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl(address)
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build();

        interactionService = retrofit.create(InteractionService.class);
    }

    public void sendNetworkRequest(Integer mascotId) {
        Log.d(TAG, "sendNetworkRequest()");

        Interaction interactionRequest = new Interaction(mascotId);

        interactionService.postInteraction(interactionRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse( Call<ResponseBody> call,  Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! " + response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( Call<ResponseBody> call,  Throwable t) {
                Log.e(TAG, "failure :(", t);
            }
        });
    }

    public void getNetworkRequest(Callback<ApiInteractionResponse> callback) {
        Log.d(TAG, "getNetworkRequest() callback = " + callback);
        interactionService.getInteractions().enqueue(callback);

    }

}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL