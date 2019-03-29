package com.example.androidfinalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FlightStatusHome extends AppCompatActivity {
    Toolbar flightStatusToolbar;
    ListView savedFlights;
    List<Flight> flights = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_home);

        flightStatusToolbar = findViewById(R.id.flightStatusToolbar);
        setSupportActionBar(flightStatusToolbar);
        getSupportActionBar().setTitle("Flight Status Tracker");


        Button searchFlights = findViewById(R.id.flightStatusSearchBtn);

        searchFlights.setOnClickListener(click -> {
            RadioGroup flightStatusRadioAirportFlight = findViewById(R.id.flightStatusRadioAirportFlight);
            int checkedTrackingMode = flightStatusRadioAirportFlight.getCheckedRadioButtonId();

            /** Verifies which Radio Button has been checked */
            switch (checkedTrackingMode){
                case R.id.flightStatusRadioAirportCode:
                    searchByDialog();
                    break;
                case R.id.flightStatusRadioFlightNumber:
                    Toast.makeText(this, "TO BE DEVELOPED", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Please select an Option", Toast.LENGTH_LONG).show();
                    break;
            }
        });

        /** Inflate ListView with flights previously saved by user */
        savedFlights = findViewById(R.id.flightStatusSavedFlights);
        Flight flight = new Flight("Gustavo", "AC090");
        Flight flight2 = new Flight("Mayra", "AC091");
        Flight flight3 = new Flight("Tony", "AC092");

        flights.add(flight);
        flights.add(flight2);
        flights.add(flight3);

        ListAdapter aListAdapter = new ListAdapter(flights, getApplicationContext());
        savedFlights.setAdapter(aListAdapter);
        aListAdapter.notifyDataSetChanged();

        ProgressBar progressBar = findViewById(R.id.flightProgressBar);
        progressBar.setVisibility(View.VISIBLE);


    }

    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menuItems) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_flightstatus_menu, menuItems);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flightStatusBtnReturn:
                // Snackbar to go back
                Snackbar snackbar = Snackbar.make(flightStatusToolbar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GO!", e -> finish());
                snackbar.show();
                break;
            case R.id.flightStatusBtnHelp:
                // Author name in dialog box
                Toast.makeText(this, "TO BE DEVELOPED", Toast.LENGTH_LONG).show();
                break;
        }
        return true;

    }

    /** Opens dialog for the user to select Arrivals or Departures at the searched airport */
    public void searchByDialog(){
        View middle = getLayoutInflater().inflate(R.layout.searchbyairport, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ImageButton arrivals = middle.findViewById(R.id.arrivalsImgBtn);
        arrivals.setOnClickListener(click -> {
            Toast.makeText(this, "Arrivals clicked", Toast.LENGTH_LONG).show();
        });

        ImageButton departures = middle.findViewById(R.id.departuresImgbtn);
        departures.setOnClickListener(click -> {
            Toast.makeText(this, "Departures clicked", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Departures
                    }
            }).setIcon(R.drawable.returnbtn)
            .setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // What to do on Departures
            }
        }).setIcon(R.drawable.msgicon)  .setView(middle);
        builder.create().show();
    }

    public void displayFlightInfo(View view){
//        for (int i=0; i < savedFlights.getChildCount(); i++){
//
//        }

        GridLayout flight = (GridLayout) view.getParent();

        Intent flightInformation = new Intent(getApplicationContext(), FlightStatusFlightInformation.class);
        startActivity(flightInformation);
    }

    public void deleteFlight(View view){
        Log.i("Delete flight", "deleting flight to be developed");
    }

    private class ListAdapter extends BaseAdapter {
        private List<Flight> flights;
        private Context ctx;
        private LayoutInflater inflater;

        public ListAdapter(List<Flight> flights, Context ctx){
            this.flights = flights;
            this.ctx = ctx;
            this.inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return flights.size();
        }

        @Override
        public Object getItem(int position) {
            return flights.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;
            newView = inflater.inflate(R.layout.flight_listview_btn, null);

            TextView flightNumber = newView.findViewById(R.id.flightStatusFlightName);
            flightNumber.setText("Flight " + flights.get(position).getFlightNumber());


            return newView;
        }


    }

}

