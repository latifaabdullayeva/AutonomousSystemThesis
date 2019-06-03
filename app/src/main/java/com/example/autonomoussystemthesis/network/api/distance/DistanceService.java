package com.example.autonomoussystemthesis.network.api.distance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DistanceService {

    @POST("distances")
    Call<ResponseBody> postDistance(@Body Distance distance); // ResponseBody -> Distance
}
