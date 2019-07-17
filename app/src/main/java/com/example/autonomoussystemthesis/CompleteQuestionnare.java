package com.example.autonomoussystemthesis;

import android.content.Intent;
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
    TextView beaconUuid, deviceType, deviceName, devicePersonality, a;

    private String beaconUuidReq, deviceTypeReq, devicePersonalityReq, deviceNameReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "CompQues");
        setContentView(R.layout.activity_complete_questionnare);

        getSupportActionBar().setTitle("Complete Quest");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonSave = findViewById(R.id.buttonBeaconSave);

        a = findViewById(R.id.passIn);
        a.setText("Comp Qa");

        beaconUuid = findViewById(R.id.passBeacon);
        beaconUuidReq = getIntent().getStringExtra("BEACONUUID");
        Log.d("Test", "Comp0 beacon: " + beaconUuidReq);
        beaconUuid.setText("Beacon UUID: \n" + beaconUuidReq);

        deviceType = findViewById(R.id.passDeviceType);
        deviceTypeReq = getIntent().getStringExtra("DEVICETYPE");
        Log.d("Test", "Comp0 deviceType: " + deviceTypeReq);
        deviceType.setText("Device Type: \n" + deviceTypeReq);

        devicePersonality = findViewById(R.id.passPersonality);
        devicePersonalityReq = getIntent().getStringExtra("PERSONALITY");
        Log.d("Test", "Comp0 devicePersonality: " + devicePersonalityReq);

        deviceName = findViewById(R.id.passDeviceName);
        deviceNameReq = getIntent().getStringExtra("DEVICENAME");
        Log.d("Test", "Comp0 deviceName: " + deviceNameReq);
//        if (deviceTypeReq.equals("Mascot")) {
        deviceName.setText("Device Name: \n" + deviceNameReq);
        deviceName.setVisibility(View.VISIBLE);

        devicePersonality.setText("Mascot Personality: \n" + devicePersonalityReq);
//        }

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

                Log.d("Test", "Comp1 beacon: " + beaconUuidReq);
                Log.d("Test", "Comp1 deviceType: " + deviceTypeReq);
                Log.d("Test", "Comp1 deviceName: " + deviceNameReq);
                Log.d("Test", "Comp1 devicePersonality: " + devicePersonalityReq);

                Intent myIntent = new Intent(CompleteQuestionnare.this, ShowAllDistances.class);

                myIntent.putExtra("DEVICEID", getIntent().getStringExtra("DEVICEID"));
                myIntent.putExtra("BEACONUUID", beaconUuidReq);
                myIntent.putExtra("DEVICETYPE", deviceTypeReq);
                myIntent.putExtra("DEVICENAME", deviceNameReq);
                myIntent.putExtra("PERSONALITY", devicePersonalityReq);

                startActivity(myIntent);

            }
        });

    }

}
