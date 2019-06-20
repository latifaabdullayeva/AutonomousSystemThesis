package com.example.autonomoussystemthesis.network.api.devices;

import com.example.autonomoussystemthesis.network.api.distance.Distance;
import com.example.autonomoussystemthesis.network.api.devices.Device;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface DeviceService {

    @POST("devices")
    Call<ResponseBody> postDevice(@Body Device device);

//    Example from Retrofit:

//    @GET("group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId);

    @GET("devices")
    Call<Device> getDevices();
}


//    Server side:

//    public org.springframework.data.domain.Page<Devices> getDevices(Pageable pageable) {
//        return devicesRepository.findAll(pageable);
//    }
