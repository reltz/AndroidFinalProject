package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class FinalProject_MainActivity extends AppCompatActivity {
    Toolbar finalProjectToolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_project_main);

        finalProjectToolbarMenu = findViewById(R.id.finalProjectToolbarMenu);
        setSupportActionBar(finalProjectToolbarMenu);
        getSupportActionBar().setTitle("Final Project");

        // Buttons for apps
        Button goToFlightStatus = findViewById(R.id.goToFlightStatus);
        goToFlightStatus.setOnClickListener(c -> {
            Intent flightStatus = new Intent(FinalProject_MainActivity.this, FlightStatusHome.class);

            startActivity(flightStatus);
        });

        Button goToNewsFeed = findViewById(R.id.goToNewsFeed);
        goToNewsFeed.setOnClickListener(c -> {
            Intent newsFeed = new Intent(FinalProject_MainActivity.this, NewsFeed_MainActivity.class);

            startActivity(newsFeed);
        });

        Button goNytimes = findViewById(R.id.goNYtimes);

        goNytimes.setOnClickListener(a-> {
            Intent nyTimes = new Intent(FinalProject_MainActivity.this,NYTimes_MainActivity.class);
            startActivity(nyTimes);
        });

        Button goToDictionary = findViewById(R.id.goToDictionary);

        goToDictionary.setOnClickListener(c-> {
            Intent goDict = new Intent(FinalProject_MainActivity.this, Dictionary_MainActivity.class);
            startActivity(goDict);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuItems) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.final_project_toolbar_menu, menuItems);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flightStatusBtn:
                Intent flightStatus = new Intent(FinalProject_MainActivity.this, FlightStatusHome.class);
                startActivity(flightStatus);
                break;
            case R.id.nyTimesBtn:
                Intent nyTimes = new Intent(FinalProject_MainActivity.this,NYTimes_MainActivity.class);
                startActivity(nyTimes);
                break;
            case R.id.newsFeedBtn:
                Intent newsFeed = new Intent(FinalProject_MainActivity.this, NewsFeed_MainActivity.class);
                startActivity(newsFeed);
                break;
            case R.id.articleSearchBtn:
                Toast.makeText(this, "TO BE DEVELOPED", Toast.LENGTH_LONG).show();
                break;
            case R.id.dictionaryBtn:
                Intent dict = new Intent(FinalProject_MainActivity.this, Dictionary_MainActivity.class);
                startActivity(dict);
                break;
        }
        return true;

    }
}
