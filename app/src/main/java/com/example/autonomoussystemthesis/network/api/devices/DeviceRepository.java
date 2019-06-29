package com.example.autonomoussystemthesis.network.api.devices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.autonomoussystemthesis.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceRepository extends AppCompatActivity {
    private final Retrofit retrofit;
    private final DeviceService deviceService;
    TextView textViewRes;

    public DeviceRepository() {
        // Write in terminal ./ngrok http 8080 in order to ger bseURL
        // TODO: always change ngrok URL
        retrofit = new Retrofit.Builder()
                .baseUrl("http://34e18733.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceService = retrofit.create(DeviceService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_distances);
    }

    public void sendNetworkRequest(Integer deviceId, String deviceName, String beaconUuid, String devicePersonality) {
        Device deviceRequest = new Device(null, deviceName, beaconUuid, devicePersonality);

        deviceService.createDevice(deviceRequest)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("DeviceRepository", "Response: " + response.body());
                        try {
                            if (response.body() != null) {
                                Log.d("DeviceRepository", "success! \n" + response.body().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("DeviceRepository", "failure :(", t);
                    }
                });
    }

    // TODO: get request does not return anything :(
    public void getNetworkRequest() {
        deviceService.getDevices().enqueue(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("DeviceRepository", "Code: " + response.code());
                    return;
                }
                ApiDevicesResponse devices = response.body();

                for (Device device : devices.getContent()) {
                    String content = device.getDeviceId();


                    Log.d("DeviceRepository", "con " + content);

                    textViewRes = findViewById(R.id.text_view_result);
                    textViewRes.append(content);

                }
            }

            @Override
            public void onFailure(Call<ApiDevicesResponse> call, Throwable t) {
                Log.d("DeviceRepository", "error loading from API");
                Log.d("DeviceRepository", t.getMessage());
            }
        });
    }
}