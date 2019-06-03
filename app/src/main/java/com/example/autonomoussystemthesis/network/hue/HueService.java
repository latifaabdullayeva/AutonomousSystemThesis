package com.example.autonomoussystemthesis.network.hue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// we use retrofit to indicate how the request should look like "http://square.github.io/retrofit/"
// Retrofit turns our HTTP API into a Java interface.
interface HueService {
    // in order to change the behavior of lamps, we need to do Put request
    @PUT("{username}/lights/{lightNumber}/state/")
    Call<ResponseBody> updateHueLamp(
            @Path("username") String username,
            @Path("lightNumber") int light,
            @Body HueRequest hueRequest
    );
}
