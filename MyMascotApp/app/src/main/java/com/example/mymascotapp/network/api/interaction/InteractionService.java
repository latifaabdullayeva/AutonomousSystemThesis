package com.example.mymascotapp.network.api.interaction;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface InteractionService {

    @POST("interactionTimes")
    Call<ResponseBody> postInteraction(@Body Interaction interaction);

    @GET("interactionTimes")
    Call<ApiInteractionResponse> getInteractions();
}
