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

public class DeviceInitialisation extends AppCompatActivity {

    protected static final String TAG = "DeviceInitialisation";

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    EditText editText;

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
}
