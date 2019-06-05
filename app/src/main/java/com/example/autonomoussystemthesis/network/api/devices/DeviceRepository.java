package com.example.autonomoussystemthesis.network.api.devices;

import android.util.Log;

import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.devices.DeviceService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceRepository {
    private final Retrofit retrofit;
    private final DeviceService deviceService;

    public DeviceRepository() {
        // Write in terminal ./ngrok http 8080 in order to ger bseURL
        retrofit = new Retrofit.Builder()
                .baseUrl("http://17c01c12.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceService = retrofit.create(DeviceService.class);
    }

    public void sendNetworkRequest(String name, String personality) {
        Device deviceRequest = new Device(name, personality);

        deviceService.postDevice(deviceRequest)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("DeviceRepository", "success!");
                        try {
                            Log.d("DeviceRepository", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("DeviceRepository", "failure :(", t);
                    }
                });
    }
}