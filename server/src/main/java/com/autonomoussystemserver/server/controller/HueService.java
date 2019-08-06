package com.autonomoussystemserver.server.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public enum HueService {
    ;

    // in order to change the behavior of lamps, we need to do Put request
    @PUT("{username}/lights/{lightNumber}/state/")
    public Call<ResponseBody> updateHueLamp(@Path("username") String username,
                                            @Path("lightNumber") int light,
                                            @Body HueRequest hueRequest) {
        return null;
    }
}
