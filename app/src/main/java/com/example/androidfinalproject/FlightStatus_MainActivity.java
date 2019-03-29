package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class FlightStatus_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flightstatus_activity_main);

        // Buttons for apps
        Button goToFlightStatus = findViewById(R.id.goToFlightStatus);
        goToFlightStatus.setOnClickListener(c -> {
            Intent flightStatus = new Intent(FlightStatus_MainActivity.this, FlightStatusHome.class);

            startActivity(flightStatus);
        });

        Button goToNewsFeed = findViewById(R.id.goToNewsFeed);
        goToNewsFeed.setOnClickListener(c -> {
            Intent newsFeed = new Intent(FlightStatus_MainActivity.this, NewsFeed_MainActivity.class);

            startActivity(newsFeed);
        });

    }
}
