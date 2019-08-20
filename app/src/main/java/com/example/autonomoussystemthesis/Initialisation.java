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
    LinearLayout devTypeLayout, personalityLayout;
    Button saveButton;
    RadioGroup radioGroupDevType, radioGroupPersonality;
    TextView beaconUuid, textViewDevType, textViewPersonality, numbOfBeacons, textView;
    EditText mascotNameEditText;
    private BeaconManager beaconManager;
    private ArrayList<String> beaconList, persList;
    private RecyclerViewAdapter adapter;
    private String beaconValue, deviceTypeValue, devicePersonalityValue, mascotValue;
    final PersonalityRepository personalityRepository = new PersonalityRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Initialisation started");
        setContentView(R.layout.activity_initialisation);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        Log.d(TAG, "beaconManager = " + beaconManager);

        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        RecyclerView beaconListView = findViewById(R.id.listViewBeacon);
        this.adapter = new RecyclerViewAdapter(this, this.beaconList);
        beaconListView.setAdapter(adapter);
        Log.d(TAG, "this.beaconList = " + this.beaconList);
        Log.d(TAG, "beaconListView = " + beaconListView);
        Log.d(TAG, "this.adapter = " + this.adapter);

        // Choose the Personality for Mascot out of list from DB
        this.persList = new ArrayList<>();
        RecyclerView persListView = findViewById(R.id.listViewPers);
        this.adapter = new RecyclerViewAdapter(this, this.persList);
        persListView.setAdapter(adapter);
        Log.d(TAG, "this.persList = " + this.persList);
        Log.d(TAG, "persListView = " + persListView);
        Log.d(TAG, "this.adapter = " + this.adapter);

        saveButton = findViewById(R.id.saveButton);
        radioGroupDevType = findViewById(R.id.radioGroupDevType);
        textViewDevType = findViewById(R.id.IntroTextDevType);

//        radioGroupPersonality = findViewById(R.id.radioGroupPer);
        textViewPersonality = findViewById(R.id.IntroTextPer);

        saveButtonListener();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick()");
//        Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);
        beaconValue = beaconList.get(position);
        Toast.makeText(Initialisation.this, "Selected Beacon: " + beaconValue, Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.showSelectedBeacon);
        textView.setText(getString(R.string.selectedBeacon, beaconValue));
        Log.d(TAG, "beaconValue = " + beaconValue);
        Log.d(TAG, "textView = " + textView);


        devicePersonalityValue = persList.get(position);
        Toast.makeText(Initialisation.this, "Selected Personality: " + devicePersonalityValue, Toast.LENGTH_SHORT).show();
        TextView textViewPer = findViewById(R.id.showSelectedPers);
        textViewPer.setText(getString(R.string.selectedPer, devicePersonalityValue));
        Log.d(TAG, "devicePersonalityValue = " + devicePersonalityValue);
        Log.d(TAG, "textViewPer = " + textViewPer);
    }

    public void checkBeaconButton() {
        Log.d(TAG, "checkBeaconButton()");
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.listViewBeacon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, beaconList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "recyclerView = " + recyclerView);
        Log.d(TAG, "adapter = " + adapter);
    }

    public void checkPersButton() {
        Log.d(TAG, "checkPersButton()");
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.listViewPers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, persList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        mascotValue = mascotNameEditText.getText().toString();
        Log.d(TAG, "recyclerView = " + recyclerView);
        Log.d(TAG, "adapter = " + adapter);
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
        // TODO: HueRepository should be discovered in ShowAllDistances Activity

        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                Log.d(TAG, "didRangeBeaconsInRegion()");

                if (beacons.size() > 0) {

                    // Show the List of all beacons
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        if (!beaconList.contains(beacon.getId1().toString())) {
                            beaconList.add(beacon.getId1().toString());
                            // if you want to get ID of beacon -> .getId1();
                            Log.d(TAG, "beaconList = " + beaconList);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    numbOfBeacons = findViewById(R.id.numbOfBeacons);
                    numbOfBeacons.setText(getString(R.string.beaconSize, beacons.size()));

                    checkBeaconButton();
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",
                    null, null, null));
        } catch (
                RemoteException ignored) {
        }
    }

    public void checkDevButton(View view) {
        int selectedRadioDevTypeId = radioGroupDevType.getCheckedRadioButtonId();
        RadioButton radioButtonDevType;
        radioButtonDevType = findViewById(selectedRadioDevTypeId);
        deviceTypeValue = radioButtonDevType.getText().toString();
        Toast.makeText(Initialisation.this, "3" + deviceTypeValue, Toast.LENGTH_SHORT).show();

        mascotNameEditText = findViewById(R.id.mascotNameEditText);

        personalityRepository.getNetworkRequest(new Callback<ApiPersonalityResponse>() {
            @Override
            public void onResponse(Call<ApiPersonalityResponse> call, Response<ApiPersonalityResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code = " + response.body());
                    Log.d("PersonalityRepository", "Code: " + response.code());
                    return;
                }
                ApiPersonalityResponse personalities = response.body();

                for (Personality personality : personalities.getContent()) {
                    Log.d(TAG, "personality.getPer_id() = " + personality.getPer_id());
                    Log.d("PersonalityRepository", "personality.getPer_id() = " + personality.getPer_id());
                }
            }

            @Override
            public void onFailure(Call<ApiPersonalityResponse> call, Throwable t) {
                Log.d(TAG, "error loading from API = " + t.getMessage());
                Log.d("PersonalityRepository", "error loading from API");
                Log.d("PersonalityRepository", t.getMessage());
            }
        });
        beaconList.clear();


        checkPersButton();
//        personalityLayout = findViewById(R.id.radioGroupPer);
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
                            saveData();
                            myIntent.putExtra("DEVICENAME", mascotValue);
                            myIntent.putExtra("PERSONALITY", devicePersonalityValue);
                        }
                    }
                    saveData();

                    if (deviceTypeValue.equals("Mascot")) {
//                        deviceRepository.sendNetworkRequest(null, mascotValue, beaconValue, personality.getPer_id());
                    } else {
//                        deviceRepository.sendNetworkRequest(null, deviceTypeValue, beaconValue, personality.getPer_id());
                    }
                    startActivity(myIntent);
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