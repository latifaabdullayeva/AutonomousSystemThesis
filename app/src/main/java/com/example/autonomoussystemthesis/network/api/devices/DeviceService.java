package com.example.autonomoussystemthesis.network.api.devices;

import com.example.autonomoussystemthesis.network.api.distance.Distance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DeviceService {

    @POST("devices")
    Call<ResponseBody> postDevice(@Body Device device);
}
