package com.example.autonomoussystemthesis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseExperiment extends AppCompatActivity {

    protected static final String TAG = "ChooseExpActivity";

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ChooseExperimentActivity started up");

        setContentView(R.layout.activity_choose_experiment);

        radioGroup = findViewById(R.id.radioGroup);
        textView = findViewById(R.id.IntroText);
        Button buttonSave = findViewById(R.id.buttonExperiment);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                textView.setText("Your choice: " + radioButton.getText());

            }
        });
        Intent myIntent = new Intent(this, NameDevice.class);
        this.startActivity(myIntent);
    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }
}
