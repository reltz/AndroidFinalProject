package com.example.androidfinalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FlightStatusFlightInformationFragment extends Fragment {
    private boolean isTablet;
    Bundle dataReceived;
    private long id;

    public TextView flightNumber;
    public TextView flightStatus;
    public TextView flightSpeedH;
    public TextView flightSpeedG;
    public TextView flightSpeedV;
    public TextView flightAltitude;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the data that was passed from ChatRoom
        dataReceived = getArguments();
        id = dataReceived.getLong(FlightStatusHome.ITEM_ID );

        View result =  inflater.inflate(R.layout.flight_status_flight_information, container, false);

//        FlightStatusToolbarMenu f = new FlightStatusToolbarMenu();
        int position = dataReceived.getInt("POSITION");

        flightNumber = result.findViewById(R.id.flight_number);
        flightNumber.setText(dataReceived.getString("FlightNumber"));

        flightStatus = result.findViewById(R.id.flight_status);
        flightStatus.setText(dataReceived.getString("FlightStatus"));

        flightSpeedH = result.findViewById(R.id.flight_speedHorizontal);
        flightSpeedH.setText(dataReceived.getString("FlightSpeedH"));

        flightSpeedG = result.findViewById(R.id.flight_speedGround);
        flightSpeedG.setText(dataReceived.getString("FlightSpeedG"));

        flightSpeedV = result.findViewById(R.id.flight_speedVertical);
        flightSpeedV.setText(dataReceived.getString("FlightSpeedV"));

        flightAltitude = result.findViewById(R.id.flight_altitude);
        flightAltitude.setText(dataReceived.getString("FlightAltitude"));

        Button saveBtn = result.findViewById(R.id.flight_status_SaveBtn);

        for(int i = 0; i < FlightStatusHome.flights.size(); i++){
            if(FlightStatusHome.flights.get(i).getFlightNumber().equalsIgnoreCase(dataReceived.getString("FlightNumber"))){
                saveBtn.setText("Delete");
            }
        }

        saveBtn.setOnClickListener(click -> {

            if(isTablet) { //both the list and details are on the screen:
                FlightStatusHome parent = (FlightStatusHome) getActivity();
//                get a database:

                if (saveBtn.getText().toString().equalsIgnoreCase("Save")) {
                    FlightStatusDbOpen dbOpener = new FlightStatusDbOpen(this.getActivity());
                    SQLiteDatabase db = dbOpener.getWritableDatabase();
                    saveFlight(db);

                    db.close();
                    dbOpener.close();
                } else {
                    FlightStatusDbOpen dbOpener = new FlightStatusDbOpen(this.getActivity());
                    SQLiteDatabase db = dbOpener.getWritableDatabase();
                    FlightStatusHome.flights.remove(position);
                    FlightStatusHome.updateFlightListView();
                    db.delete(FlightStatusDbOpen.TABLE_NAME, FlightStatusDbOpen.COL_ID + "=?", new String[]{Long.toString(dataReceived.getInt(FlightStatusHome.ITEM_ID))});
                    Toast.makeText(parent.getApplicationContext(), "Deleting", Toast.LENGTH_LONG).show();
                    parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                    parent.finish();

                    db.close();
                    dbOpener.close();
                }


            } else {

                FlightStatusFlightInformationEmpty parent2 = (FlightStatusFlightInformationEmpty) getActivity();
                Toolbar toolbar = result.findViewById(R.id.flightStatusToolbarFlightInfo);
                parent2.setSupportActionBar(toolbar);
                parent2.getSupportActionBar().setTitle("Flight " + dataReceived.getString("FlightNumber") + " Info");
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("SaveOrDelete", saveBtn.getText());
                backToFragmentExample.putExtra("FlightNum", dataReceived.getString("FlightNumber"));
                backToFragmentExample.putExtra("position", position);
//                backToFragmentExample.putExtra(FlightStatusHome.ITEM_ID, dataReceived.getLong(FlightStatusHome.ITEM_ID ));
                if(saveBtn.getText().toString().equalsIgnoreCase("Save")){
//                    FlightStatusHome parent = (FlightStatusHome) getActivity();
                    FlightStatusDbOpen dbOpener = new FlightStatusDbOpen(this.getActivity());
                    SQLiteDatabase db = dbOpener.getWritableDatabase();

                    saveFlight(db);
                    db.close();
                    dbOpener.close();
                }

                parent2.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent2.finish(); //go back
            }
        });

        return result;
    }

    public void saveFlight(SQLiteDatabase db){
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

        FlightStatusHome.flights.add(msg);
        FlightStatusHome.updateFlightListView();
    }
}
