package com.example.mymascotapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymascotapp.network.api.devices.ApiDevicesResponse;
import com.example.mymascotapp.network.api.devices.Device;
import com.example.mymascotapp.network.api.devices.DeviceRepository;
import com.example.mymascotapp.network.api.distance.DistanceRepository;
import com.example.mymascotapp.network.api.personality.ApiPersonalityResponse;
import com.example.mymascotapp.network.api.personality.Personality;
import com.example.mymascotapp.network.api.personality.PersonalityRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Initialisation extends AppCompatActivity implements BeaconConsumer, RecyclerViewAdapter.ItemClickListener {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text1";
    public static final String TEXT2 = "text2";
    public static final String TEXT3 = "text3";
    public static final String TEXT4 = "text4";

    protected static final String TAG = "InitialisationActivity";

    DistanceRepository distanceRepository;
    DeviceRepository deviceRepository;
    PersonalityRepository personalityRepository;

    LinearLayout personalityLayout;
    Button saveButton;
    RadioGroup radioGroupDevType, radioGroupPersonality;
    TextView textViewDevType, textViewPersonality, textViewSelectedBeacon;
    EditText mascotNameEditText;

    private BeaconManager beaconManager;
    private ArrayList<String> beaconList, deviceList, tempBeaconList;
    private RecyclerViewAdapter adapter;
    private String beaconValue, devicePersonalityValue, mascotValue;
    private String deviceTypeValue = "Mascot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation);
        Log.d("FLOW", "Initialisation");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String serverAddress = getIntent().getExtras()
                .getString("serverAddress");

        distanceRepository = new DistanceRepository(serverAddress);
        deviceRepository = new DeviceRepository(serverAddress);
        personalityRepository = new PersonalityRepository(serverAddress);

        this.deviceList = new ArrayList<>();
        this.tempBeaconList = new ArrayList<>();
        textViewSelectedBeacon = findViewById(R.id.showSelectedBeacon);
        textViewSelectedBeacon.setText(getString(R.string.selectedBeacon, ""));

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        RecyclerView beaconListView = findViewById(R.id.listViewBeacon);
        this.adapter = new RecyclerViewAdapter(this, this.beaconList);
        beaconListView.setAdapter(adapter);

        saveButton = findViewById(R.id.saveButton);
        radioGroupDevType = findViewById(R.id.radioGroupDevType);
        textViewDevType = findViewById(R.id.IntroTextDevType);

        radioGroupPersonality = findViewById(R.id.radioGroupPersonality);
        textViewPersonality = findViewById(R.id.IntroTextPer);

        saveButtonListener();
    }

    @Override
    public void onItemClick(View view, int position) {
        beaconValue = beaconList.get(position);
        textViewSelectedBeacon.setText(getString(R.string.selectedBeacon, beaconValue));
    }

    public void checkBeaconButton() {
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.listViewBeacon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, beaconList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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
                    Log.d(TAG, "deviceList = " + deviceList);
                }

                beaconManager.addRangeNotifier((beacons, region) -> {
                    beaconList.clear();

                    if (beacons.size() > 0) {
                        for (Beacon beacon : beacons) {
//                                Log.d(TAG, "beacons = " + beacons.toString());
//                                Log.d(TAG, "beacon = " + beacon.getId1().toString());

                            if (!tempBeaconList.contains(beacon.getId1().toString())) {
                                tempBeaconList.add(beacon.getId1().toString());
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
                        checkBeaconButton();
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

    public void checkDevButton(View view) {
        int selectedRadioDevTypeId = radioGroupDevType.getCheckedRadioButtonId();
        RadioButton radioButtonDevType;
        radioButtonDevType = findViewById(selectedRadioDevTypeId);
//        deviceTypeValue = radioButtonDevType.getText().toString();

        mascotNameEditText = findViewById(R.id.mascotNameEditText);
        personalityLayout = findViewById(R.id.radioGroupPersonality);
    }

    public void checkPerButton(View view) {
        int selectedRadioPersId = radioGroupPersonality.getCheckedRadioButtonId();
        RadioButton radioButtonPersonality;
        radioButtonPersonality = findViewById(selectedRadioPersId);
        mascotValue = "";
        if (deviceTypeValue != null && deviceTypeValue.equals("Mascot")) {
            mascotValue = mascotNameEditText.getText().toString();
            devicePersonalityValue = radioButtonPersonality.getText().toString();
            saveButtonListener();
        }
    }

    public void saveButtonListener() {
        saveButton.setOnClickListener(v -> {
            int selectedRadioDevTypeId = radioGroupDevType.getCheckedRadioButtonId();
            if (selectedRadioDevTypeId == -1) {
                Toast.makeText(Initialisation.this, "No Type for Device selected", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton radioButtonDevType;
                radioButtonDevType = findViewById(selectedRadioDevTypeId);
                deviceTypeValue = radioButtonDevType.getText().toString();

                String serverAddress = getIntent().getExtras()
                        .getString("serverAddress");

                Intent intent = new Intent(this, ShowAllDistances.class);

                intent.putExtra("serverAddress", serverAddress);
                intent.putExtra("BEACONUUID", beaconValue);
                intent.putExtra("DEVICETYPE", deviceTypeValue);


                if (deviceTypeValue.equals("Mascot")) {
                    int selectedRadioPersId = radioGroupPersonality.getCheckedRadioButtonId();
                    if (selectedRadioPersId == -1) {
                        Toast.makeText(Initialisation.this, "No Personality for Device selected", Toast.LENGTH_SHORT).show();
                    } else {
                        // maybeSolved TODO: DO NOT DELETE: may need it later
//                            saveData();
                        intent.putExtra("DEVICENAME", mascotValue);
                        intent.putExtra("PERSONALITY", devicePersonalityValue);
                    }
                }
                if (deviceTypeValue.equals("Mascot")) {
                    personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiPersonalityResponse> call, @NonNull Response<ApiPersonalityResponse> response) {
                            Log.d(TAG, response.toString());
                            if (!response.isSuccessful()) {
                                Log.d(TAG, "PersonalityRepository Code: " + response.code());
                                return;
                            }
                            ApiPersonalityResponse personalities = response.body();

                            if (personalities != null) {
                                for (Personality personality : personalities.getContent()) {
                                    Log.d(TAG, "personality = " + personality);
                                    Log.d(TAG, "Chosen devicePersonalityValue = " + devicePersonalityValue);
                                    Log.d(TAG, "personality.getPersonality_name() = " + personality.getPersonality_name());
                                    Log.d(TAG, "personality.getPer_id() = " + personality.getId());
                                    Log.d(TAG, "personality.getHue_color() = " + personality.getHue_color());

                                    if (personality.getPersonality_name().equals(devicePersonalityValue)) {
                                        Log.d(TAG, "personality.getPer_id() = " + personality.getId());
                                        Integer personalityId = personality.getId();
                                        Log.d(TAG, "mascotValue = " + mascotValue);
                                        Log.d(TAG, "beaconValue = " + beaconValue);
                                        Log.d(TAG, "personalityId = " + personalityId);
                                        Log.d(TAG, "sendNetworkRequest");
                                        deviceRepository.sendNetworkRequest(null, mascotValue, deviceTypeValue, beaconValue, personalityId);
                                        Log.d(TAG, "personalityId = " + personalityId);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiPersonalityResponse> call, @NonNull Throwable t) {
                            Log.d(TAG, "error loading from API: " + t.getMessage());
                        }
                    });
                } else {
                    deviceRepository.sendNetworkRequest(null, null, deviceTypeValue, beaconValue, null);
                    Toast.makeText(Initialisation.this, "Other than Mascot no one can have Personality", Toast.LENGTH_SHORT).show();
                }
                // Before starting  the ShowAllDIst Activity, check if the deviceRepository.sendNetworkRequest was successful or not
                // The way how I check, I do getRequest and check if the beacon that I have saved is in the table.
                // If not, then I will consider the request as failed
                Log.d(TAG, "getNetworkRequest");
                deviceRepository.getNetworkRequest(new Callback<ApiDevicesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiDevicesResponse> call, @NonNull Response<ApiDevicesResponse> response) {
                        Log.d(TAG, "getNetworkRequest = " + response.toString());
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "getNetworkRequest DeviceRepository Code: " + response.code());
                            return;
                        }
                        ApiDevicesResponse devicesResponse = response.body();
                        if (devicesResponse != null) {
                            for (Device device : devicesResponse.getContent()) {
                                Log.d(TAG, "getNetworkRequest device = " + device);
                                Log.d(TAG, "getNetworkRequest device.getBeaconUuid() = " + device.getBeaconUuid());
                                // The way how I check, I do getRequest and check if the beacon that I have saved is in the table.
                                // If not, then I will consider the request as failed
                                if (device.getBeaconUuid().equals(beaconValue)) {
                                    saveData();
                                    Toast.makeText(Initialisation.this, "You choice was successfully saved! :)", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "getNetworkRequest RESPONSE WAS NOT SUCCESSFUL :(");
                                    // Show a user a message that we could not save your data
                                    Toast.makeText(Initialisation.this, "SOMETHING WENT WRONG :(\n We could not save your data", Toast.LENGTH_SHORT).show();
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
                startActivity(intent);
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1, beaconValue);
        editor.putString(TEXT2, deviceTypeValue);
        editor.putString(TEXT3, mascotValue);
        editor.putString(TEXT4, devicePersonalityValue);
        editor.apply();
    }
}