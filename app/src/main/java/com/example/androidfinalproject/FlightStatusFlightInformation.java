package com.example.androidfinalproject;
/***********************************************************************************
 Class:    FlightStatusFlightInformation
 Purpose:  This activity will display flights information in detail
 Author:   Gustavo Adami
 Course:   Android
 Data members:   none
 Methods: onCreate(Bundle savedInstanceState) : void
 *************************************************************************************/

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FlightStatusFlightInformation extends AppCompatActivity {
    private boolean isTablet;
    Bundle dataReceived;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    /** FlightStatusFlightInformation onCreate -> Retrieves and displays layout*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_flight_information);

        //get the data that was passed from Home
        Bundle dataToPass = getIntent().getExtras();


//        FlightStatusToolbarMenu f = new FlightStatusToolbarMenu();
        int position = dataReceived.getInt("POSITION");

//        Toolbar toolbar = result.findViewById(R.id.flightStatusToolbarFlightInfo);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Flight " + dataReceived.getString("FlightNumber") + " Info");

        TextView flightNumber = findViewById(R.id.flight_number);
        flightNumber.setText(dataToPass.getString("FlightNumber"));

        TextView flightStatus = findViewById(R.id.flight_status);
        flightStatus.setText(dataToPass.getString("FlightStatus"));

        TextView flightSpeedH = findViewById(R.id.flight_speedHorizontal);
        flightSpeedH.setText(dataToPass.getString("FlightSpeedH"));

        TextView flightSpeedG = findViewById(R.id.flight_speedGround);
        flightSpeedG.setText(dataToPass.getString("FlightSpeedG"));

        TextView flightSpeedV = findViewById(R.id.flight_speedVertical);
        flightSpeedV.setText(dataToPass.getString("FlightSpeedV"));

        TextView flightAltitude = findViewById(R.id.flight_altitude);
        flightAltitude.setText(dataToPass.getString("FlightAltitude"));

        Button saveBtn = findViewById(R.id.flight_status_SaveBtn);

        for(int i = 0; i < FlightStatusHome.flights.size(); i++){
            if(FlightStatusHome.flights.get(i).getFlightNumber().equalsIgnoreCase(dataReceived.getString("FlightNumber"))){
                saveBtn.setText("Delete");
            }
        }

        saveBtn.setOnClickListener(click -> {
            //get a database:
            FlightStatusDbOpen dbOpener = new FlightStatusDbOpen((Activity)getApplicationContext());
            SQLiteDatabase db = dbOpener.getWritableDatabase();
            if(saveBtn.getText().toString().equalsIgnoreCase("Save")) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string name in the NUMBER column:
                newRowValues.put(FlightStatusDbOpen.COL_NUMBER, flightNumber.getText().toString());
                //put string email in the STATUS column:
                newRowValues.put(FlightStatusDbOpen.COL_STATUS, flightStatus.getText().toString());
                //put string email in the SPEEDH column:
                newRowValues.put(FlightStatusDbOpen.COL_SPEEDHORIZONTAL, flightSpeedH.getText().toString());
                //put string email in the SPEEDG column:
                newRowValues.put(FlightStatusDbOpen.COL_SPEEDISGROUND, flightSpeedG.getText().toString());
                //put string email in the SPEEDV column:
                newRowValues.put(FlightStatusDbOpen.COL_SPEEDVERTICAL, flightSpeedV.getText().toString());
                //put string email in the ALTITUDE column:
                newRowValues.put(FlightStatusDbOpen.COL_ALTITUDE, flightAltitude.getText().toString());
                //insert in the database:
                int newId = (int) db.insert(FlightStatusDbOpen.TABLE_NAME, null, newRowValues);

                Flight msg = new Flight(newId, flightNumber.getText().toString(), flightStatus.getText().toString(), flightSpeedH.getText().toString(), Integer.valueOf(flightSpeedG.getText().toString()) != 0, flightSpeedV.getText().toString(), flightAltitude.getText().toString());

//                FlightStatusHome parent = (FlightStatusHome) getApplicationContext();
                FlightStatusHome.flights.add(msg);
                FlightStatusHome.updateFlightListView();


//            FlightStatusArrivalsInformation parent = (FlightStatusArrivalsInformation) getAc

                Toast.makeText(getApplicationContext(), "Flight successfully added", Toast.LENGTH_LONG).show();
//                finish();
            } else {
                FlightStatusHome.flights.remove(position);
                FlightStatusHome.updateFlightListView();
                db.delete(FlightStatusDbOpen.TABLE_NAME, FlightStatusDbOpen.COL_ID + "=?", new String[]{Long.toString(dataReceived.getInt(FlightStatusHome.ITEM_ID))});
                Toast.makeText(getApplicationContext(), "Deleting", Toast.LENGTH_LONG).show();
//                finish();
            }
        });
    }
}
