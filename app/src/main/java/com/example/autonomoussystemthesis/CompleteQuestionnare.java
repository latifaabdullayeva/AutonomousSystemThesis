package com.example.autonomoussystemthesis;

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


        deviceName = findViewById(R.id.passDeviceName);
        String deviceNameReq = getIntent().getStringExtra("DEVICENAME");
        if (deviceTypeReq.equals("Mascot")) {
            deviceName.setText("Device Name: \n" + deviceNameReq);
            deviceName.setVisibility(View.VISIBLE);
        }

        devicePersonality = findViewById(R.id.passPersonality);
        String devicePersonalityReq = getIntent().getStringExtra("PERSONALITY");
        devicePersonality.setText("Mascot Personality: \n" + devicePersonalityReq);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CompleteQuestionnare", deviceTypeReq);
                if (deviceTypeReq.equals("Mascot")) {
                    Log.d("CompleteQuestionnare", deviceTypeReq + beaconUuidReq + devicePersonalityReq);
                    deviceRepository.sendNetworkRequest(deviceNameReq, beaconUuidReq, devicePersonalityReq);
                    Toast.makeText(CompleteQuestionnare.this, "Your data saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    deviceRepository.sendNetworkRequest(deviceTypeReq, beaconUuidReq, devicePersonalityReq);
                    Toast.makeText(CompleteQuestionnare.this, "Your data saved successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
