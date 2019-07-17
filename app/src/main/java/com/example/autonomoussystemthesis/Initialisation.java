package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Initialisation extends AppCompatActivity {
    protected static final String TAG = "InitialisationActivity";
    RadioGroup radioGroupDevType;
    RadioButton radioButtonDevType;
    TextView textViewDevType;
    private String deviceTypeValue;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialisation);
        radioGroupDevType = findViewById(R.id.radioGroupDevType);
        textViewDevType = findViewById(R.id.IntroTextDevType);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkButton(View view) {
        int radioIdType = radioGroupDevType.getCheckedRadioButtonId();
        radioButtonDevType = findViewById(radioIdType);
        deviceTypeValue = radioButtonDevType.getText().toString();
        Button buttonSave = findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Initialisation.this, MainActivity.class);
                myIntent.putExtra("DEVICETYPE", deviceTypeValue);
//                startActivity(myIntent);
                saveData();
                loadData();
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, deviceTypeValue);
        editor.apply();
        Toast.makeText(Initialisation.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        deviceTypeValue = sharedPreferences.getString(TEXT, "");
    }
}

/*
1) Тебе надо сохранять инфу о том выбрал ли пользователь deviceType уже или нет.
Для этого, когда user выбирает его в первый раз, тебе надо сохранять deviceType в shared preferences

2) В MainActivity, тебе надо считывать значение deviceType из shared preferences.
Если оно существует, запускай AllDistances сразу. Если нет, тогда Initialisation
 */














/*
package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;
import com.example.autonomoussystemthesis.network.hue.HueRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.round;

public class Initialisation extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "InitialisationActivity";

    private BeaconManager beaconManager;

    private ArrayList<String> beaconList;
    private ListView beaconListView;
    private ArrayAdapter<String> adapter;

    RadioGroup radioGroupDevType, radioGroupPer;
    RadioButton radioButtonDevType, radioButtonPer;
    TextView beaconUuid, textViewDevType, textViewPer, numbOfBeacons;
    EditText mascotNameEditText;
    LinearLayout perLayout;

    private TextView textView;
    private String deviceTypeValue;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Initialisation started up");

        setContentView(R.layout.activity_initialisation);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // Choose the Beacon Device out of list
        this.beaconList = new ArrayList<>();
        this.beaconListView = findViewById(R.id.listViewBeacon);
        this.adapter = new ArrayAdapter<>(this, R.layout.my_listview_radiobutton_layout, this.beaconList);
        this.beaconListView.setAdapter(adapter);

        Log.d("Test", "Init0 deviceTypeValue: " + deviceTypeValue);

        radioGroupDevType = findViewById(R.id.radioGroupDevType);
        textViewDevType = findViewById(R.id.IntroTextDevType);

        radioGroupPer = findViewById(R.id.radioGroupPer);
        textViewPer = findViewById(R.id.IntroTextPer);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
//         in order to discover the IpAdress of the bridge, we used https://discovery.meethue.com/
//         Once we have the address load the test app by visiting https://<bridge ip address>/debug/clip.html
//         We need to use the randomly generated username that the bridge creates for you.
//         Fill in the info below and press the POST button, after that press the link button on bridge.
//         URL	/api
//         Body	{"devicetype":"newDeveloper"}
//         Method	POST
//         the Command Response will show you a username
//         The documentation of Hue Api is "https://developers.meethue.com/develop/get-started-2/"
        final HueRepository hueRepository = new HueRepository(
                "192.168.0.100",
                "vY5t4oArH-K0BUA7430cb1rJ8mC1DYMzkmBWRr91"
        );
        final DistanceRepository distanceRepository = new DistanceRepository();
        final DeviceRepository deviceRepository = new DeviceRepository();


        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                    // Show the List of all beacons
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        if (!beaconList.contains(beacon.getId1().toString())) {
                            beaconList.add(beacon.getId1().toString()); // if you want to get ID of beacon -> .getId1();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    numbOfBeacons = findViewById(R.id.numbOfBeacons);
                    numbOfBeacons.setText("Number of Beacon devices: " + beacons.size());

//                  Show in logs the number of beacons that app found
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    for (Beacon beacon : beacons) {

//                        deviceRepository.sendNetworkRequest("Nexus", beacon.toString(), "intimate");
                        Log.d(TAG, "The beacon " + beacon.toString());
//                        distanceRepository.sendNetworkRequest(1, 2, round(beacon.getDistance() * 100));

                        int brightness = (int) beacon.getDistance() * 80;
                        if (brightness > 255) {
                            brightness = 255;
                        }
//                        hueRepository.updateBrightness(brightness);

                        // set extra data field for beacon, name each beacon according to device
//                        beacon = new Beacon.Builder().setExtraDataFields(Arrays
//                                .asList(1L, 2L, 3L, 4L)).build();

                        // get from DB all devices in the system, and beacon.toString()
//                        measurementBeaconList = (TextView) findViewById(R.id.measurementText);
//                        measurementBeaconList.setText("The number of beacons: " + beacons.size() +
//                                "\nThe beacon [" + beacon + "] is about " + round(beacon.getDistance() * 100) + " cm away.\n" +
//                                beacon.getBluetoothAddress() + "\n" +
//                                beacon.getExtraDataFields());

                        if (beacon.getDistance() <= 0.45) { // intimate
                            Log.d(TAG, "Intimate Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                            Log.d(TAG, "-");
//                          TODO: it should send the distance to all mascots to the DATABASE

//                            deviceRepository.getNetworkRequest();

                            // TODO: vibration
                            // START: When you click on VIBRATE button, phone vibrates
//                            Button vibrationButton = findViewById(R.id.vibtationButton);
//                            final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                            vibrationButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (Build.VERSION.SDK_INT >= 26) {
//                                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
//                                    } else {
//                                        vibrator.vibrate(200);
//                                    }
//                                }
//                            });

                        } else if (beacon.getDistance() >= 0.46 && beacon.getDistance() <= 1.21) { // personal
                                Log.d(TAG, "Personal Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                                Log.d(TAG, "-");
                                // TODO: tablet color
                                } else if (beacon.getDistance() >= 1.22 && beacon.getDistance() <= 3.70) { // social
                                Log.d(TAG, "Social Zone!!!! " + round(beacon.getDistance() * 100) + " cm away.");
                                Log.d(TAG, "-");
                                // TODO: bench is here, lights
//                            hueRepository.updateBrightness(90);
                                } else if (beacon.getDistance() > 3.70) { // public
                                Log.d(TAG, "Public Zone!!!! " + round(beacon.getDistance() * 100) + " cm away."); // !!! a bilo String.format("%.2f", firstBeacon.getDistance())
                                Log.d(TAG, "-");
                                // TODO: speakers
                                }
                                }
                                Log.d(TAG, "------------------------------------------------------------");
                                }
                                }
                                });
                                try {
                                beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
                                } catch (
                                RemoteException ignored) {
                                }
                                }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
        this.finish();
        }
        return super.onOptionsItemSelected(item);
        }

public void checkButton(View view) {
        int radioIdType = radioGroupDevType.getCheckedRadioButtonId();
        radioButtonDevType = findViewById(radioIdType);
        deviceTypeValue = radioButtonDevType.getText().toString();
        mascotNameEditText = findViewById(R.id.mascotNameEditText);
        perLayout = findViewById(R.id.perLayout);

        if (radioButtonDevType.getText().toString().equals("Mascot")) {
//          When user chooses the Mascot, then we show EditText, so he can name his mascot
        mascotNameEditText.setVisibility(View.VISIBLE);
        perLayout.setVisibility(View.VISIBLE);


        int radioIdPer = radioGroupPer.getCheckedRadioButtonId();
        radioButtonPer = findViewById(radioIdPer);

        } else {
//          When user chooses again other types, then we hide again EditText and Personality Layout
        mascotNameEditText.setVisibility(View.INVISIBLE);
        perLayout.setVisibility(View.INVISIBLE);
        }

        Button buttonSave = findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        // Bind onclick event handler
        beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {

        Toast.makeText(Initialisation.this, "Selected Beacon: " +
        beaconList.get(position), Toast.LENGTH_SHORT).show();

        if (radioButtonDevType.getText().toString().equals("Mascot")) {

        String mascotValue = mascotNameEditText.getText().toString();
        String perValue = radioButtonPer.getText().toString();

        Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);

        String deviceValue = beaconList.get(position);
        myIntent.putExtra("BEACONUUID", deviceValue);
        myIntent.putExtra("DEVICETYPE", deviceTypeValue);
        myIntent.putExtra("DEVICENAME", mascotValue);
        myIntent.putExtra("PERSONALITY", perValue);

        startActivity(myIntent);
        } else {
        Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);

        String deviceValue = beaconList.get(position);
        myIntent.putExtra("BEACONUUID", deviceValue);
        myIntent.putExtra("DEVICETYPE", deviceTypeValue);
        myIntent.putExtra("DEVICENAME", "none");
        myIntent.putExtra("PERSONALITY", "none");
        startActivity(myIntent);
        }
        saveData();
        Log.d("Test", "Init1 deviceTypeValue: " + deviceTypeValue);

        loadData();
        Log.d("Test", "Init2 deviceTypeValue: " + deviceTypeValue);
        }
        });
        }
        });
        }

public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.d("Test", "Init3 deviceTypeValue: " + deviceTypeValue);

        editor.putString(TEXT, deviceTypeValue);

        editor.apply();
        Toast.makeText(Initialisation.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
        }

public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        deviceTypeValue = sharedPreferences.getString(TEXT, "");

        Log.d("Test", "Init4 deviceTypeValue: " + deviceTypeValue);
        }

        }

 */