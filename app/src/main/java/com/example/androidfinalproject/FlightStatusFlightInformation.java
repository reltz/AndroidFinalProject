package com.example.androidfinalproject;
/***********************************************************************************
 Class:    FlightStatusFlightInformation
 Purpose:  This activity will display flights information in detail
 Author:   Gustavo Adami
 Course:   Android
 Data members:   none
 Methods: onCreate(Bundle savedInstanceState) : void
 *************************************************************************************/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FlightStatusFlightInformation extends AppCompatActivity {

    /** FlightStatusFlightInformation onCreate -> Retrieves and displays layout*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_flight_information);

        //get the data that was passed from ChatRoom
        Bundle dataToPass = getIntent().getExtras();

        TextView flightNumber = findViewById(R.id.flight_number);
        flightNumber.setText(dataToPass.getString("Flight Number"));

        TextView flightStatus = findViewById(R.id.flight_status);
        flightStatus.setText(dataToPass.getString("Flight Status"));




    }
}
