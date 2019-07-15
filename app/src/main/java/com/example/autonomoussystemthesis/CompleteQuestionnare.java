package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;

public class CompleteQuestionnare extends AppCompatActivity {
    final DeviceRepository deviceRepository = new DeviceRepository();
    TextView beaconUuid, deviceType, deviceName, devicePersonality;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String beacUUID = "BEACONUUID";
    public static final String devType = "DEVICETYPE";
    public static final String devName = "DEVICENAME";
    public static final String devPersonality = "PERSONALITY";

    private String bID;
    private String dType;
    private String dName;
    private String dPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_questionnare);
        Button buttonSave = findViewById(R.id.buttonBeaconSave);

        beaconUuid = findViewById(R.id.passBeacon);
        String beaconUuidReq = getIntent().getStringExtra("BEACONUUID");
        beaconUuid.setText("Beacon UUID: \n" + beaconUuidReq);

        deviceType = findViewById(R.id.passDeviceType);
        String deviceTypeReq = getIntent().getStringExtra("DEVICETYPE");
        deviceType.setText("Device Type: \n" + deviceTypeReq);

        devicePersonality = findViewById(R.id.passPersonality);
        String devicePersonalityReq = getIntent().getStringExtra("PERSONALITY");

        deviceName = findViewById(R.id.passDeviceName);
        String deviceNameReq = getIntent().getStringExtra("DEVICENAME");
        if (deviceTypeReq.equals("Mascot")) {
            deviceName.setText("Device Name: \n" + deviceNameReq);
            deviceName.setVisibility(View.VISIBLE);

            devicePersonality.setText("Mascot Personality: \n" + devicePersonalityReq);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CompleteQuestionnare", deviceTypeReq);
                if (deviceTypeReq.equals("Mascot")) {
                    Log.d("CompleteQuestionnare", deviceTypeReq + beaconUuidReq + devicePersonalityReq);
                    deviceRepository.sendNetworkRequest(null, deviceNameReq, beaconUuidReq, devicePersonalityReq);
                } else {
                    deviceRepository.sendNetworkRequest(null, deviceTypeReq, beaconUuidReq, null);
                }

                Toast.makeText(CompleteQuestionnare.this, "Your data saved successfully!", Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(CompleteQuestionnare.this, ShowAllDistances.class);

                myIntent.putExtra("DEVICEID", getIntent().getStringExtra("DEVICEID"));

                myIntent.putExtra("BEACONUUID", getIntent().getStringExtra("BEACONUUID"));
                myIntent.putExtra("DEVICETYPE", getIntent().getStringExtra("DEVICETYPE"));
                myIntent.putExtra("DEVICENAME", getIntent().getStringExtra("DEVICENAME"));
                myIntent.putExtra("PERSONALITY", getIntent().getStringExtra("PERSONALITY"));

                startActivity(myIntent);

                saveData();

            }
        });
        loadData();
        updateViews();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(beacUUID, getIntent().getStringExtra("BEACONUUID"));
        editor.putString(devType, getIntent().getStringExtra("DEVICETYPE"));
        editor.putString(devName, getIntent().getStringExtra("DEVICENAME"));
        editor.putString(devPersonality, getIntent().getStringExtra("PERSONALITY"));

        editor.apply();
        Toast.makeText(CompleteQuestionnare.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        bID = sharedPreferences.getString(beacUUID, "");
        dType = sharedPreferences.getString(devType, "");
        dName = sharedPreferences.getString(devName, "");
        dPer = sharedPreferences.getString(devPersonality, "");
    }

    public void updateViews() {
        beaconUuid.setText(bID);
        deviceType.setText(dType);
        deviceName.setText(dName);
        devicePersonality.setText(dPer);

    }
}
