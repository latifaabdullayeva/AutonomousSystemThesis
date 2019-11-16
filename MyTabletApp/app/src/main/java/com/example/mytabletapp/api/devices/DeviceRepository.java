package com.example.mytabletapp.api.devices;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mytabletapp.R;
import com.example.mytabletapp.api.personality.Personality;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
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

    public DeviceRepository(String address) {

        //        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:8080/")
        // IP uni: "http://10.20.130.243:8080/"
        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder().baseUrl(address)
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);
        ListView resultListView = findViewById(R.id.list_view_result);

        ArrayList<String> resultList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.my_listview_radiobutton_layout, resultList);
        resultListView.setAdapter(adapter);
    }

    public void sendNetworkRequest(Integer deviceId, String deviceName, String deviceType, String beaconUuid, Integer devicePersonality) {
//        Intent myIntent = new Intent(DeviceRepository.this, Initialisation.class);

        Device deviceRequest;
        if (devicePersonality != null) {
            Personality personality = new Personality(devicePersonality);
            deviceRequest = new Device(null, deviceName, deviceType, beaconUuid, personality);
        } else {
            deviceRequest = new Device(null, deviceName, deviceType, beaconUuid, null);
        }

        deviceService.createDevice(deviceRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "failure :(", t);
            }
        });
    }

    public void getNetworkRequest(Callback<ApiDevicesResponse> callback) {
        deviceService.getDevices().enqueue(callback);
    }
}
// Write in terminal ( ./ngrok http 8080 ) in order to ger bseURL