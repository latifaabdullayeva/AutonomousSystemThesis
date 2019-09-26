package com.example.mytabletapp.api.devices;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface DeviceService {

    @POST("devices")
    Call<ResponseBody> createDevice(@Body Device device);

    @GET("devices")
    Call<ApiDevicesResponse> getDevices();

}