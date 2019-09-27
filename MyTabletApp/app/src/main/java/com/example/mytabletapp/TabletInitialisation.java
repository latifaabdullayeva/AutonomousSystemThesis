package com.example.mytabletapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytabletapp.api.devices.ApiDevicesResponse;
import com.example.mytabletapp.api.devices.Device;
import com.example.mytabletapp.api.devices.DeviceRepository;
import com.example.mytabletapp.api.distance.DistanceRepository;
import com.example.mytabletapp.api.interaction.InteractionRepository;
import com.example.mytabletapp.api.personality.PersonalityRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabletInitialisation extends AppCompatActivity
        implements BeaconConsumer, RecyclerViewAdapter.ItemClickListener {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text1";
    public static final String TEXT2 = "text2";

    protected static final String TAG = "TabletInitialisation";

    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    final PersonalityRepository personalityRepository = new PersonalityRepository();
    final InteractionRepository interactionRepository = new InteractionRepository();

    private BeaconManager beaconManager;
    private TextView numbOfBeacons;

    private ArrayList<String> beaconList, deviceList, tempBeaconList;
    private RecyclerViewAdapter adapter;

    String deviceTypeValue = "Tablet";
    String beaconValue;
    TextView textViewSelectedBeacon;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet_initialisation);

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tablet Initialisation");
        actionBar.setDisplayHomeAsUpEnabled(true);
        // TODO: add back Button and functionality for it

        this.deviceList = new ArrayList<>();
        this.tempBeaconList = new ArrayList<>();
        textViewSelectedBeacon = findViewById(R.id.showSelectedBeacon);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        RecyclerView beaconListView = findViewById(R.id.listViewBeacon);
        this.adapter = new RecyclerViewAdapter(this, this.beaconList);
        beaconListView.setAdapter(adapter);

        saveButton = findViewById(R.id.buttonBeaconSave);

        saveButtonListener();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent myIntent = new Intent(TabletInitialisation.this, BackgroundColorChange.class);
        beaconValue = beaconList.get(position);
        Toast.makeText(TabletInitialisation.this, "Selected Beacon: " + beaconValue, Toast.LENGTH_SHORT).show();
        textViewSelectedBeacon.setText(getString(R.string.selectedBeacon, beaconValue));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                    return;
                }

                ApiDevicesResponse devices = response.body();
                for (Device device : Objects.requireNonNull(devices).getContent()) {
                    deviceList.add(device.getBeaconUuid());
                }

                beaconManager.addRangeNotifier((beacons, region) -> {
                    beaconList.clear();

                    if (beacons.size() > 0) {
                        for (Beacon beacon : beacons) {
                            if (!tempBeaconList.contains(beacon.getId1().toString())) {
                                tempBeaconList.add(beacon.getId1().toString());
                                // if you want to get ID of beacon -> .getId1();
                                // maybeSolved TODO: do not show this beacon if it is already in the database
                            }
                        }

                        for (String device : deviceList) {
                            tempBeaconList.remove(device);
                        }

                        beaconList.addAll(tempBeaconList);

                        if (beaconList.isEmpty()) {
                            textViewSelectedBeacon.setText(getString(R.string.selectedBeacon, "No beacons in our range"));
                        }

                        runOnUiThread(() -> adapter.notifyDataSetChanged());

                        // set up the RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.listViewBeacon);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TabletInitialisation.this));
                        adapter = new RecyclerViewAdapter(TabletInitialisation.this, beaconList);
                        adapter.setClickListener(TabletInitialisation.this);
                        recyclerView.setAdapter(adapter);
                    }
                });

                try {
                    beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
                } catch (RemoteException ignored) {
                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiDevicesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "error loading from API... " + t.getMessage());
            }
        });
    }

    private void saveButtonListener() {
        saveButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(TabletInitialisation.this, BackgroundColorChange.class);
            myIntent.putExtra("BEACONUUID", beaconValue);
            myIntent.putExtra("DEVICETYPE", deviceTypeValue);

            deviceRepository.sendNetworkRequest(null, null, deviceTypeValue, beaconValue, null);

            // Before starting  the ShowAllDIst Activity, check if the deviceRepository.sendNetworkRequest was successful or not
            // The way how I check, I do getRequest and check if the beacon that I have saved is in the table.
            // If not, then I will consider the request as failed
            deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "getNetworkRequest DeviceRepository Code: " + response.code());
                        return;
                    }
                    ApiDevicesResponse devicesResponse = response.body();
                    if (devicesResponse != null) {
                        for (Device device : devicesResponse.getContent()) {
                            // The way how I check, I do getRequest and check if the beacon that I have saved is in the table.
                            // If not, then I will consider the request as failed
                            if (device.getBeaconUuid().equals(beaconValue)) {
                                saveData();
                                startActivity(myIntent);
                            } else {
                                Log.d(TAG, "getNetworkRequest RESPONSE WAS NOT SUCCESSFUL :(");
                                // Show a user a message that we could not save your data
                                Toast.makeText(TabletInitialisation.this, "SOMETHING WENT WRONG :(\n We could not save your data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.d(TAG, "getNetworkRequest devicesResponse is NULL");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiDevicesResponse> call, @NonNull Throwable t) {

                }
            });
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1, beaconValue);
        editor.putString(TEXT2, deviceTypeValue);
        editor.apply();
        Toast.makeText(TabletInitialisation.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }
}