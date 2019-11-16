package com.example.mytabletapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mytabletapp.api.devices.DeviceRepository;
import com.example.mytabletapp.api.distance.DistanceRepository;
import com.example.mytabletapp.api.interaction.InteractionRepository;
import com.example.mytabletapp.api.personality.PersonalityRepository;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalityInitialisation extends AppCompatActivity {

    protected static final String TAG = "PersInitialisation";

    DistanceRepository distanceRepository;
    DeviceRepository deviceRepository;
    PersonalityRepository personalityRepository;
    InteractionRepository interactionRepository;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_initialisation);

        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Personality Initialisation");
        actionBar.setDisplayHomeAsUpEnabled(true);

        String serverAddress = getIntent().getExtras()
                .getString("serverAddress");

        distanceRepository = new DistanceRepository(serverAddress);
        deviceRepository = new DeviceRepository(serverAddress);
        personalityRepository = new PersonalityRepository(serverAddress);
        interactionRepository = new InteractionRepository(serverAddress);

        personalityRepository.sendNetworkRequestPers(null, "Openness",
                "orange", 254, 3488, 220, "#FF9800",
                3, "pop");

        personalityRepository.sendNetworkRequestPers(null, "Conscientiousness",
                "violet", 254, 49460, 150, "#673AB7",
                4, "rock");

        personalityRepository.sendNetworkRequestPers(null, "Extroversion",
                "pink", 254, 57805, 198, "#F436BE",
                5, "classic");

        personalityRepository.sendNetworkRequestPers(null, "Agreeableness",
                "blue", 254, 47110, 253, "#03A9F4",
                2, "soul");

        personalityRepository.sendNetworkRequestPers(null, "Neuroticism",
                "yellow", 254, 12828, 52, "#FFEB3B",
                1, "jazz");

        nextButton = findViewById(R.id.nextButton);
        nextButtonListener();
    }

    private void nextButtonListener() {
        nextButton.setOnClickListener(v -> {
            String serverAddress = getIntent().getExtras()
                    .getString("serverAddress");

            Intent myIntent = new Intent(PersonalityInitialisation.this, LampInitialisation.class);
            myIntent.putExtra("serverAddress", serverAddress);

            startActivity(myIntent);
        });
    }
}