package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.DeviceDto;
import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.repository.DevicesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sun.rmi.runtime.Log;

import java.io.IOException;

// GET --> POST
@RestController
public class DevicesController {

    // The @Autowired annotation allows you to skip configurations elsewhere of what to inject and just does it for you
    @Autowired
    private DevicesRepository devicesRepository;

    // maps HTTP GET requests onto specific handler methods. It is a composed annotation that acts as a shortcut
    // for @RequestMapping(method = RequestMethod.GET).
    @GetMapping("/devices")
    public Page<Devices> getDevices(Pageable pageable) {
        return devicesRepository.findAll(pageable);
    }

    @PostMapping("/devices")
    public ResponseEntity<Devices> createDevice(@RequestBody DeviceDto deviceDto) {

        Devices existingDevice = devicesRepository.findByBeacon(deviceDto.getBeaconUuid());

        if (existingDevice != null) {
            return ResponseEntity.badRequest()
                    .body(null);
        } else {
            Devices newDevice = new Devices();
            newDevice.setDeviceName(deviceDto.getDeviceName());
            newDevice.setBeaconUuid(deviceDto.getBeaconUuid());
            newDevice.setDevicePersonality(deviceDto.getDevicePersonality());

            devicesRepository.save(newDevice);
            return ResponseEntity.ok(newDevice);
        }
        // TODO: problem to solve
        // query all hue lamps from database
        // send request to all hue lamps to turn light on
    }

    private final HueService hueService;
    private final String username;

    public DevicesController(String ipAddress, String user) {
//         The Retrofit class generates an implementation of the HueService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//         By default, Retrofit can only deserialize HTTP bodies into OkHttp's ResponseBody type
//         and it can only accept its RequestBody type for @Body.
//         Converters can be added to support other types. Gson: com.squareup.retrofit2:converter-gson
//         GsonConverterFactory class is needed to generate an implementation of the HueService interface
//         which uses Gson for its deserialization.
        hueService = retrofit.create(HueService.class);
        username = user;
    }

    public void updateBrightness(int brightness) {
        HueRequest request = new HueRequest(true, brightness);

        hueService.updateHueLamp(username, 1, request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        System.out.println("HueRepository" + " success!");
//                        try {
//                            if (response.body() != null) {
//                                System.out.println("HueRepository" + response.body());
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        System.out.println("HueRepository" + " failure : " + t);
                    }
                });
    }
}