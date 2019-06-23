package com.example.autonomoussystemthesis.network.api.devices;

import android.util.Log;

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
        // TODO: always change ngrok URL
        retrofit = new Retrofit.Builder()
                .baseUrl("http://7f3bdf20.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceService = retrofit.create(DeviceService.class);
    }

    public void sendNetworkRequest(String deviceName, String beaconUuid, String devicePersonality) {
        Device deviceRequest = new Device(deviceName, beaconUuid, devicePersonality);

        deviceService.createDevice(deviceRequest)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("DeviceRepository", "Response: " + response.body());
                        try {
                            if (response.body() != null) {
                                Log.d("DeviceRepository", "success! \n" + response.body().string());
                            }
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

//    // TODO: get request does not return anything :(
//    public void getNetworkRequest() {
//        deviceService.getDevices().enqueue(new Callback<Device>() {
//            @Override
//            public void onResponse(Call<Device> call, Response<Device> response) {
//                Log.d("DeviceRepository", "GET success!");
//            }
//
//            @Override
//            public void onFailure(Call<Device> call, Throwable t) {
//                Log.d("DeviceRepository", "error loading from API");
//
//            }
//        });
//    }
}