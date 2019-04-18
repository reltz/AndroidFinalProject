package com.example.androidfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlightStatusFlightInformationEmpty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_flight_information_empty);

        //get the data that was passed from ChatRoom
        Bundle dataToPass = getIntent().getExtras();

        //This is copied directly from FragmentExample.java lines 47-54
        FlightStatusFlightInformationFragment dFragment = new FlightStatusFlightInformationFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flightStatusFragment, dFragment)
                .addToBackStack("Flight Info Details")
                .commit();
    }
}
