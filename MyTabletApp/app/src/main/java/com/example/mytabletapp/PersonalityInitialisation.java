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
//    InteractionRepository interactionRepository;

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
//        interactionRepository = new InteractionRepository(serverAddress);

        personalityRepository.sendNetworkRequestPers(null, "Openness",
                "OrangeYellow", 253, 12299, 251, "#fdac22",
                3, "sophisticated");

        personalityRepository.sendNetworkRequestPers(null, "Conscientiousness",
                "GreenBlue", 253, 34152, 254, "#50ffab",
                4, "unpretentious");

        personalityRepository.sendNetworkRequestPers(null, "Extroversion",
                "RedOrange", 253, 4008, 252, "#fb4c0c",
                5, "contemporary");

        personalityRepository.sendNetworkRequestPers(null, "Agreeableness",
                "PinkWhite", 242, 52489, 87, "#ffbfc8",
                2, "unpretentious");

        personalityRepository.sendNetworkRequestPers(null, "Neuroticism",
                "RedBlack", 38, 271, 252, "#d90001",
                1, "contemporary");

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