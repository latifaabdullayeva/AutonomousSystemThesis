package com.example.autonomoussystemthesis.network.api.devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.autonomoussystemthesis.CompleteQuestionnare;
import com.example.autonomoussystemthesis.R;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceRepository extends AppCompatActivity {
    private final DeviceService deviceService;
    final DistanceRepository distanceRepository = new DistanceRepository();

    private ArrayList<String> resultList;
    private ArrayAdapter<String> adapter;

    public DeviceRepository() {
        // Write in terminal ./ngrok http 8080 in order to ger bseURL
        // TODO: always change ngrok URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://36c387e7.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceService = retrofit.create(DeviceService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_distances);
        ListView resultListView = findViewById(R.id.list_view_result);

        this.resultList = new ArrayList<>();
        this.adapter = new ArrayAdapter<>(this, R.layout.my_listview_radiobutton_layout, this.resultList);
        resultListView.setAdapter(adapter);
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
                Log.d("DeviceRepository", "res: " + response);
                if (!response.isSuccessful()) {
                    Log.d("DeviceRepository", "Code: " + response.code());
                    return;
                }
                ApiDevicesResponse devices = response.body();


                for (Device device : Objects.requireNonNull(devices).getContent()) {
                    String content = device.getDeviceId() + "\n";
                    content += device.getBeaconUuid() + "\n";
                    content += device.getDeviceName() + "\n";
                    content += device.getDevicePersonality() + "\n";

                    Log.d("DeviceRepository", "con " + device.getDeviceId());


                    Intent myIntent = new Intent(DeviceRepository.this, CompleteQuestionnare.class);
                    myIntent.putExtra("DEVICEID", device.getDeviceId());

                    distanceRepository.sendNetworkRequest(device.getDeviceId(), device.getDeviceId(), 2147483649L);
                    //Проблема тут. Тебе надо передать в intent список deviceId.
                    // Вместо этого ты передаешь только одно значение которое перезаписывается в цикле.
                    //
                    //Тебе нужно в том цикле собрать все deviceId в список, и потом передать их через intent вне цикла.

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