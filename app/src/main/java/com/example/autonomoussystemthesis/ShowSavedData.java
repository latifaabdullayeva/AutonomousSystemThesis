package com.example.autonomoussystemthesis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowSavedData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_data);

        getSupportActionBar().setTitle("Saved Data");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}