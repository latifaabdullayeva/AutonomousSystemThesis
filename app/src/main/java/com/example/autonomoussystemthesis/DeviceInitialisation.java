package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;
import com.example.autonomoussystemthesis.network.api.distance.DistanceRepository;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import static java.lang.Math.round;

public class DeviceInitialisation extends AppCompatActivity implements BeaconConsumer {

    protected static final String TAG = "DeviceInitialisation";

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    EditText editText;

    private BeaconManager beaconManager;
    private TextView beaconsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DeviceInitialisation started up");
        setContentView(R.layout.activity_device_initialisation);

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.IntroText);

        Button buttonSave = findViewById(R.id.buttonDeviceName);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DeviceInitialisation.this, PersonalityInitialisation.class);
                startActivity(myIntent);
            }
        });
        getSupportActionBar().setTitle("Device Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected " + radioButton.getText(), Toast.LENGTH_SHORT).show();
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
        final DistanceRepository distanceRepository = new DistanceRepository();
        final DeviceRepository deviceRepository = new DeviceRepository();


        beaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {


                if (beacons.size() > 0) {
//                  Show in logs the number of beacons that app found
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());

                    for (Beacon beacon : beacons) {
                        beaconsList = (Button) findViewById(R.id.beaconsList);
                        beaconsList.setText("The number of beacons: " + beacon.toString() + "\n");
                        Log.d(TAG, "The beacon " + beacon.toString());
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException e) {
        }
    }

}


//public class DeviceInitialisation extends AppCompatActivity {
//
//    protected static final String TAG = "DeviceInitialisation";
//
//    RadioGroup radioGroup;
//    RadioButton radioButton;
//    TextView textView;
//    EditText editText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "DeviceInitialisation started up");
//
//        setContentView(R.layout.activity_device_initialisation);
//
//        radioGroup = findViewById(R.id.radioGroup);
//        textView = findViewById(R.id.IntroText);
//
//        Button buttonSave = findViewById(R.id.buttonDeviceName);
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(DeviceInitialisation.this, PersonalityInitialisation.class);
//                startActivity(myIntent);
//            }
//        });
//        getSupportActionBar().setTitle("Device Initialisation");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            this.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void checkButton(View view) {
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton = findViewById(radioId);
//        Toast.makeText(this, "Selected " + radioButton.getText(), Toast.LENGTH_SHORT).show();
//    }
//}
