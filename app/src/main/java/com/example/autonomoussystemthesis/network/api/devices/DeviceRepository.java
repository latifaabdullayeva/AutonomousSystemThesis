package com.example.autonomoussystemthesis.network.api.devices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.autonomoussystemthesis.R;
import com.example.autonomoussystemthesis.network.api.personality.Personality;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceRepository extends AppCompatActivity {
    protected static final String TAG = "DeviceRepository";
    private final DeviceService deviceService;

    public DeviceRepository() {
        Log.d("FLOW", "DeviceRepository");

        //        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
                .client(
                        new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build();

        deviceService = retrofit.create(DeviceService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "DeviceRepository");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_distances);
        ListView resultListView = findViewById(R.id.list_view_result);

        ArrayList<String> resultList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.my_listview_radiobutton_layout, resultList);
        resultListView.setAdapter(adapter);
    }

    public void sendNetworkRequest(Integer deviceId, String deviceName, String deviceType, String beaconUuid, Integer devicePersonality) {
        Log.d(TAG, "sendNetworkRequest");
//        Log.d(TAG, "DevRequestSTATUS");
//        Intent myIntent = new Intent(DeviceRepository.this, Initialisation.class);
        Personality personality = new Personality(devicePersonality);
        Device deviceRequest = new Device(null, deviceName, deviceType, beaconUuid, personality);
        deviceService.createDevice(deviceRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "Response: " + response.body());
                try {
                    if (response.body() != null) {
                        Log.d(TAG, "success! \n" + response.body().string());
//                        myIntent.putExtra("DevRequestSTATUS", "success");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "failure :(", t);
            }
        });
    }

    public void getNetworkRequest(Callback<ApiDevicesResponse> callback) {
        Log.d(TAG, "getNetworkRequest");
        deviceService.getDevices().enqueue(callback);
    }
}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL