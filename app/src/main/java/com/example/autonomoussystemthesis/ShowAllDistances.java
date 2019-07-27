package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.autonomoussystemthesis.network.api.devices.ApiDevicesResponse;
import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllDistances extends AppCompatActivity {
    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "ShowALlDist");
        setContentView(R.layout.activity_show_all_distances);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Distances");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView beaconTag = findViewById(R.id.passBeaconUUID);
        String beaconTagValue = getIntent().getStringExtra("BEACONUUID");
        beaconTag.setText(beaconTagValue);

        TextView deviceType = findViewById(R.id.passDeviceType);
        String deviceTypeValue = getIntent().getStringExtra("DEVICETYPE");
        deviceType.setText(deviceTypeValue);

        TextView mascotName = findViewById(R.id.passMascotName);
        String mascotNameValue = getIntent().getStringExtra("DEVICENAME");
        mascotName.setText(mascotNameValue);

        TextView devicePers = findViewById(R.id.passPersonality);
        String devicePersValue = getIntent().getStringExtra("PERSONALITY");
        devicePers.setText(devicePersValue);

        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
                Log.d("DeviceRepository", "res: " + response);
                if (!response.isSuccessful()) {
                    Log.d("DeviceRepository", "Code: " + response.code());
                    return;
                }
                ApiDevicesResponse devices = response.body();

                // find my own phone (device id)
                Device myDevice = null;
                for (Device device : devices.getContent()) {
                    if (device.getBeaconUuid().equals(beaconTagValue)) {
                        myDevice = device;
                    }
                }

                if (myDevice != null) {
                    for (Device device : Objects.requireNonNull(devices).getContent()) {
                        Log.d("DeviceRepository", "con " + device.getDeviceId());

                        distanceRepository.sendNetworkRequest(myDevice.getDeviceId(), device.getDeviceId(), 2147483649L);

                        // distanceRepository.sendNetworkRequest(1, deviceRepository.getNetworkRequest(), round(beacon.getDistance() * 100));
                        //Проблема тут. Тебе надо передать в intent список deviceId.
                        // Вместо этого ты передаешь только одно значение которое перезаписывается в цикле.
                        //
                        //Тебе нужно в том цикле собрать все deviceId в список, и потом передать их через intent вне цикла.
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDevicesResponse> call, Throwable t) {
                Log.d("DeviceRepository", "error loading from API");
                Log.d("DeviceRepository", t.getMessage());
            }
        });

        // TODO:
        // Here the app starts measuring a distance to all other devices in the system

    }
}