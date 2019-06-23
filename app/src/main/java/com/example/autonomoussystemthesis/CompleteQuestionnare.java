package com.example.autonomoussystemthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.autonomoussystemthesis.network.api.devices.DeviceRepository;

public class CompleteQuestionnare extends AppCompatActivity {
    final DeviceRepository deviceRepository = new DeviceRepository();
    TextView beaconUuid, deviceName, devicePersonality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_questionnare);
        Button buttonSave = findViewById(R.id.buttonBeaconSave);

        beaconUuid = findViewById(R.id.passBeacon);
        beaconUuid.setText("Beacon UUID: \n" + getIntent().getStringExtra("BEACONUUID"));

        deviceName = findViewById(R.id.passDeviceName);
        deviceName.setText("Device Name: \n" + getIntent().getStringExtra("DEVICENAME"));

        devicePersonality = findViewById(R.id.passPersonality);
        devicePersonality.setText("Mascot Personality: \n" + getIntent().getStringExtra("PERSONALITY"));


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceRepository.sendNetworkRequest(
                        getIntent().getStringExtra("DEVICENAME"),
                        getIntent().getStringExtra("BEACONUUID"),
                        getIntent().getStringExtra("PERSONALITY")
                );
            }
        });
    }
}
