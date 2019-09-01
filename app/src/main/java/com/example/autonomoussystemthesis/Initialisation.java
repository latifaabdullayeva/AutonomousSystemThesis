package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
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

import com.example.autonomoussystemthesis.network.api.devices.ApiDevicesResponse;
import com.example.autonomoussystemthesis.network.api.devices.Device;
import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;
import com.example.autonomoussystemthesis.network.api.personality.ApiPersonalityResponse;
import com.example.autonomoussystemthesis.network.api.personality.Personality;
import com.example.autonomoussystemthesis.network.api.personality.PersonalityRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
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

    final DistanceRepository distanceRepository = new DistanceRepository();
    final DeviceRepository deviceRepository = new DeviceRepository();
    final PersonalityRepository personalityRepository = new PersonalityRepository();

    LinearLayout personalityLayout;
    Button saveButton;
    RadioGroup radioGroupDevType, radioGroupPersonality;
    TextView textViewDevType, textViewPersonality, textViewSelectedBeacon;
    EditText mascotNameEditText;

    private BeaconManager beaconManager;
    private ArrayList<String> beaconList, deviceList, tempBeaconList;
    private RecyclerViewAdapter adapter;
    private String beaconValue, deviceTypeValue, devicePersonalityValue, mascotValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation);
        Log.d("FLOW", "Initialisation");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.deviceList = new ArrayList<>();
        this.tempBeaconList = new ArrayList<>();
        textViewSelectedBeacon = findViewById(R.id.showSelectedBeacon);

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
        Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);
        beaconValue = beaconList.get(position);
        Toast.makeText(Initialisation.this, "Selected Beacon: " + beaconValue, Toast.LENGTH_SHORT).show();
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
            public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code: " + response.code());
                    return;
                }

                ApiDevicesResponse devices = response.body();
                for (Device device : devices.getContent()) {
                    deviceList.add(device.getBeaconUuid());
                    Log.d(TAG, "deviceList = " + deviceList);
                }

                beaconManager.addRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                        beaconList.clear();

                        if (beacons.size() > 0) {
                            for (Beacon beacon : beacons) {
                                Log.d(TAG, "beacons = " + beacons.toString());
                                Log.d(TAG, "beacon = " + beacon.getId1().toString());

                                if (!tempBeaconList.contains(beacon.getId1().toString())) {
                                    tempBeaconList.add(beacon.getId1().toString());
                                    // if you want to get ID of beacon -> .getId1();
                                    // TODO: do not show this beacon if it is already in the database
                                    // TODO: Get list of beacons that are in DB
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
                    }
                });

                try {
                    beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
                } catch (RemoteException ignored) {
                }

            }

            @Override
            public void onFailure(Call<ApiDevicesResponse> call, Throwable t) {
                Log.d(TAG, "error loading from API... " + t.getMessage());
            }
        });
    }

    public void checkDevButton(View view) {
        int selectedRadioDevTypeId = radioGroupDevType.getCheckedRadioButtonId();
        RadioButton radioButtonDevType;
        radioButtonDevType = findViewById(selectedRadioDevTypeId);
        deviceTypeValue = radioButtonDevType.getText().toString();
        Toast.makeText(Initialisation.this, "3" + deviceTypeValue, Toast.LENGTH_SHORT).show();

        mascotNameEditText = findViewById(R.id.mascotNameEditText);
        personalityLayout = findViewById(R.id.radioGroupPersonality);
    }

    public void checkPerButton(View view) {
        int selectedRadioPersId = radioGroupPersonality.getCheckedRadioButtonId();
        RadioButton radioButtonPersonality;
        radioButtonPersonality = findViewById(selectedRadioPersId);
        mascotValue = mascotNameEditText.getText().toString();
        devicePersonalityValue = radioButtonPersonality.getText().toString();
        Toast.makeText(Initialisation.this, "2" + devicePersonalityValue, Toast.LENGTH_SHORT).show();
        saveButtonListener();
    }

    public void saveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioDevTypeId = radioGroupDevType.getCheckedRadioButtonId();
                if (selectedRadioDevTypeId == -1) {
                    Toast.makeText(Initialisation.this, "No Type for Device selected", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButtonDevType;
                    radioButtonDevType = findViewById(selectedRadioDevTypeId);
                    Toast.makeText(Initialisation.this, "1" + radioButtonDevType.getText(), Toast.LENGTH_SHORT).show();
                    deviceTypeValue = radioButtonDevType.getText().toString();
                    Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);
                    myIntent.putExtra("BEACONUUID", beaconValue);
                    myIntent.putExtra("DEVICETYPE", deviceTypeValue);
                    if (deviceTypeValue.equals("Mascot")) {
                        int selectedRadioPersId = radioGroupPersonality.getCheckedRadioButtonId();
                        if (selectedRadioPersId == -1) {
                            Toast.makeText(Initialisation.this, "No Personality for Device selected", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: DO NOT DELETE: may need it later
//                            saveData();
                            myIntent.putExtra("DEVICENAME", mascotValue);
                            myIntent.putExtra("PERSONALITY", devicePersonalityValue);
                        }
                    }
                    if (deviceTypeValue.equals("Mascot")) {
                        personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
                            @Override
                            public void onResponse(Call<ApiPersonalityResponse> call, Response<ApiPersonalityResponse> response) {
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
                            public void onFailure(Call<ApiPersonalityResponse> call, Throwable t) {
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
                        public void onResponse(Call<ApiDevicesResponse> call, Response<ApiDevicesResponse> response) {
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
                                        startActivity(myIntent);
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
                        public void onFailure(Call<ApiDevicesResponse> call, Throwable t) {

                        }
                    });
//                    saveData();
//                    startActivity(myIntent);
                }
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
        Toast.makeText(Initialisation.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }
}