package com.example.mytabletapp.api.personality;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface PersonalityService {

    @POST("personality")
    Call<ResponseBody> createPersonality(@Body Personality personality);

    @GET("personality")
    Call<ApiPersonalityResponse> getPersonality();

}
