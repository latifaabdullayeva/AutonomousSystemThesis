package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalityInitialisation extends AppCompatActivity {

    protected static final String TAG = "ChoosePerActivity";

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;

    TextView beaconUuid, deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ChoosePerActivity started up");

        setContentView(R.layout.activity_personality_initialisation);

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.IntroText);

        getSupportActionBar().setTitle("Personality");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconUuid = findViewById(R.id.passBeacon);
        beaconUuid.setText("Beacon UUID: \n" + getIntent().getStringExtra("BEACONUUID"));

        deviceName = findViewById(R.id.passDeviceName);
        deviceName.setText("Device Name: \n" + getIntent().getStringExtra("DEVICENAME"));
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
        Toast.makeText(this, "Selected " + radioButton.getText(), Toast.LENGTH_LONG).show();

        Button buttonSave = findViewById(R.id.buttonPersonality);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PersonalityInitialisation.this, CompleteQuestionnare.class);

                String perValue = radioButton.getText().toString();
                myIntent.putExtra("BEACONUUID", getIntent().getStringExtra("BEACONUUID"));
                myIntent.putExtra("DEVICENAME", getIntent().getStringExtra("DEVICENAME"));
                myIntent.putExtra("PERSONALITY", perValue);

                startActivity(myIntent);
            }
        });
    }
}