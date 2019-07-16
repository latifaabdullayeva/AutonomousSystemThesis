package com.example.autonomoussystemthesis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAllDistances extends AppCompatActivity {
    private TextView beaconUuid; // beaconUuid -> textView
    private String beaconUuidReq; // beaconUuidReq -> editText

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BeacUUID = "beaconUUID"; // BeacUUID -> Text

    private String beacon; //text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_distances);

        getSupportActionBar().setTitle("All Distances");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconUuid = findViewById(R.id.passBeaconUUID);
        beaconUuidReq = getIntent().getStringExtra("DEVICETYPE");

        Log.d("Test", "Show0 beaconUuidReq: " + beaconUuidReq);
        beaconUuid.setText(beaconUuidReq);

        saveData();
//        loadData();
//        updateViews();

        Log.d("Test", "Show1 beaconUuid: " + beaconUuidReq);

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.d("Test", "Show2 beaconUuid: " + beaconUuidReq);

        editor.putString(BeacUUID, beaconUuid.getText().toString());

        editor.apply();
        Toast.makeText(ShowAllDistances.this, "Data SAVED!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        beacon = sharedPreferences.getString(BeacUUID, "");

        Log.d("Test", "Show3 beaconUuid: " + beacon);
    }

    public void updateViews() {
        Log.d("Test", "Show4 beaconUuid: " + beaconUuidReq);

        beaconUuid.setText(beacon);

    }
}