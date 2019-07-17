package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Objects;

public class DeviceInitialisation extends AppCompatActivity {

    protected static final String TAG = "DeviceInitialisation";

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView beaconUuid, textView;
    EditText mascotNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestActivity", "DevInit");

        setContentView(R.layout.activity_device_initialisation);

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.IntroText);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Device Initialisation");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconUuid = findViewById(R.id.passBeacon);
//        beaconUuid.setText("Beacon UUID: \n" + getIntent().getStringExtra("BEACONUUID"));

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
        String deviceValue = radioButton.getText().toString();

        if (radioButton.getText().toString().equals("Mascot")) {
//          When user chooses the Mascot, then we show EditText, so he can name his mascot
            mascotNameEditText = findViewById(R.id.mascotNameEditText);
            mascotNameEditText.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(DeviceInitialisation.this, "Selected " + radioButton.getText(), Toast.LENGTH_SHORT).show();
        }

        Button buttonSave = findViewById(R.id.buttonDeviceName);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.getText().toString().equals("Mascot")) {
//                    mascotNameEditText.setText("Mascot Name: \n" + getIntent().getStringExtra("DEVICENAME"));
                    Toast.makeText(DeviceInitialisation.this, "Selected " + mascotNameEditText.getText(), Toast.LENGTH_SHORT).show();
                    String mascotValue = mascotNameEditText.getText().toString();
                    Intent myIntent = new Intent(DeviceInitialisation.this, PersonalityInitialisation.class);

                    myIntent.putExtra("BEACONUUID", getIntent().getStringExtra("BEACONUUID"));
                    myIntent.putExtra("DEVICETYPE", deviceValue);
                    myIntent.putExtra("DEVICENAME", mascotValue);

                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(DeviceInitialisation.this, CompleteQuestionnare.class);

                    myIntent.putExtra("BEACONUUID", getIntent().getStringExtra("BEACONUUID"));
                    myIntent.putExtra("DEVICETYPE", deviceValue);
//                    myIntent.putExtra("PERSONALITY", "none");

                    startActivity(myIntent);
                }

            }
        });
    }


}
