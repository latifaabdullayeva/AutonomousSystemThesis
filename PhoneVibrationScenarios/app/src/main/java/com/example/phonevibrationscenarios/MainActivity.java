package com.example.phonevibrationscenarios;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button button1, button2, button3, button4, button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Phone Vibration");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        button1 = findViewById(R.id.scenario1);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /*
                Pass in an array of ints that are the durations for which to turn on or off the vibrator in milliseconds.
                    * The first value indicates the number of milliseconds to wait before turning the vibrator on.
                    * The next value indicates the number of milliseconds for which to keep the vibrator on before turning it off.
                    * Subsequent values alternate between durations in milliseconds to turn the vibrator off or to turn the vibrator on.
                    * To cause the pattern to repeat, pass the index into the pattern array at which to start the repeat, or -1 to disable repeating.

                    1000 milliseconds = 1 second

                    For example, after you clicked the button, It waits 4 seconds to start a vibration,
                    then it vibrates 200 milliseconds => stops for 3 seconds (3000 milliseconds),
                    then it vibrates 100 milliseconds => stops for 4 seconds (4000 milliseconds),

                */
                long[] pattern = {4000, 200, 3000, 100, 4000, 200, 3000, 200, 3000, 200, 3000};
                vibrator.vibrate(pattern, -1);
            }
        });

        button2 = findViewById(R.id.scenario2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                long[] pattern = {4000, 200, 3000, 100, 4000, 200, 3000, 200, 3000, 200, 3000};
                vibrator.vibrate(pattern, -1);
            }
        });

        button3 = findViewById(R.id.scenario3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                long[] pattern = {4000, 200, 3000, 100, 4000, 200, 3000, 200, 3000, 200, 3000};
                vibrator.vibrate(pattern, -1);
            }
        });

        button4 = findViewById(R.id.scenario4);
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                long[] pattern = {4000, 200, 3000, 100, 4000, 200, 3000, 200, 3000, 200, 3000};
                vibrator.vibrate(pattern, -1);
            }
        });

        button5 = findViewById(R.id.scenario5);
        button5.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                long[] pattern = {4000, 200, 3000, 100, 4000, 200, 3000, 200, 3000, 200, 3000};
                vibrator.vibrate(pattern, -1);
            }
        });
    }
}