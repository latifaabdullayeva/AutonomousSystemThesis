package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Initialisation extends AppCompatActivity {
    protected static final String TAG = "InitialisationActivity";

    RadioGroup radioGroupDevType, radioGroupPer;
    RadioButton radioButtonDevType, radioButtonPer;
    TextView beaconUuid, textViewDevType, textViewPer;
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

        Log.d("Test", "Init0 deviceTypeValue: " + deviceTypeValue);

        radioGroupDevType = findViewById(R.id.radioGroupDevType);
        textViewDevType = findViewById(R.id.IntroTextDevType);

        radioGroupPer = findViewById(R.id.radioGroupPer);
        textViewPer = findViewById(R.id.IntroTextPer);

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
                if (radioButtonDevType.getText().toString().equals("Mascot")) {

                    String mascotValue = mascotNameEditText.getText().toString();
                    String perValue = radioButtonPer.getText().toString();

                    Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);

                    myIntent.putExtra("DEVICETYPE", deviceTypeValue);
                    myIntent.putExtra("DEVICENAME", mascotValue);
                    myIntent.putExtra("PERSONALITY", perValue);

                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Initialisation.this, ShowAllDistances.class);

                    myIntent.putExtra("DEVICETYPE", deviceTypeValue);
                    myIntent.putExtra("DEVICENAME", "none");
                    myIntent.putExtra("PERSONALITY", "none");

                    startActivity(myIntent);
                }
//                saveData();
                Log.d("Test", "Init1 deviceTypeValue: " + deviceTypeValue);

//                loadData();
                Log.d("Test", "Init2 deviceTypeValue: " + deviceTypeValue);
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
