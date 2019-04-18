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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlightStatusArrivalsInformation extends AppCompatActivity {
    public static final String ITEM_SELECTED = "FlightNumber";
    public static final String ITEM_STATUS = "FlightStatus";
    public static final String ITEM_SPEEDHORIZONTAL = "FlightSpeedH";
    public static final String ITEM_SPEEDGROUND = "FlightSpeedG";
    public static final String ITEM_SPEEDVERTICAL = "FlightSpeedV";
    public static final String ITEM_ALTITUDE = "FlightAltitude";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int FLIGHT_DETAIL_ACTIVITY = 345;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_arrivals_information);

        toolbar = findViewById(R.id.flightStatusToolbarArrDep);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Arrivals at Airport");

        Bundle bundle = getIntent().getExtras();
        String stringArrivals = bundle.getString("arrivals_list");

        boolean isTablet = findViewById(R.id.flightStatusDetailFragment) != null; //check if the FrameLayout is loaded

        JSONArray arrivalsJson = null;
        try {
            arrivalsJson = new JSONArray(stringArrivals);
        } catch (JSONException e) {
            Log.e("JSON Crash at act :( !!", e.getMessage());
        }

        ArrayList<JSONObject> arrivals = new ArrayList<>();

        for (int i = 0; i < arrivalsJson.length(); i++) {
            try {
                arrivals.add(new JSONObject(arrivalsJson.getJSONObject(i).toString()));
            } catch (JSONException e) {
                Log.e("Feeding arrivals :( !!", e.getMessage());
            }
        }

        ListView flights = findViewById(R.id.flightStatusAirportArrivals);

        ListAdapter aListAdapter = new ListAdapter(arrivals, getApplicationContext());
        flights.setAdapter(aListAdapter);

        flights.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            try {
                dataToPass.putString(ITEM_SELECTED, arrivals.get(position).getJSONObject("flight").getString("iataNumber"));
                dataToPass.putString(ITEM_STATUS, arrivals.get(position).getString("status"));
                dataToPass.putString(ITEM_SPEEDHORIZONTAL, arrivals.get(position).getJSONObject("speed").getString("horizontal"));
                dataToPass.putString(ITEM_SPEEDGROUND, arrivals.get(position).getJSONObject("speed").getString("isGround"));
                dataToPass.putString(ITEM_SPEEDVERTICAL, arrivals.get(position).getJSONObject("speed").getString("vertical"));
                dataToPass.putString(ITEM_ALTITUDE, arrivals.get(position).getJSONObject("geography").getString("altitude"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet){
                FlightStatusFlightInformationFragment dFragment = new FlightStatusFlightInformationFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.flightStatusFragment, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("Flight Details") //make the back button undo the transaction
                        .commit(); //actually load the fragment.

            } else {
                Intent flightInformation = new Intent(FlightStatusArrivalsInformation.this, FlightStatusFlightInformationEmpty.class);
                flightInformation.putExtras(dataToPass);
                startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);
            }

//            Intent flightInformation = new Intent(FlightStatusArrivalsInformation.this, FlightStatusFlightInformation.class);
//            flightInformation.putExtras(dataToPass);
//            startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);
        });

    }

    /** Adapter class for ListView saved flights */
    private class ListAdapter extends BaseAdapter {
        private List<JSONObject> flights;
        private Context ctx;
        private LayoutInflater inflater;

        public ListAdapter(List<JSONObject> flights, Context ctx){
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
//            flight1DepartureFrom = flight1Json.getJSONObject("departure").getString("iataCode");

            try {
//                for(int i = 0; i < flights.size(); i++)
                if(!flights.get(position).getJSONObject("flight").getString("iataNumber").equals(""))
                    flightNumber.setText(flights.get(position).getJSONObject("flight").getString("iataNumber"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return newView;
        }

    }

    /** Inflates the toolbar in FlightStatusHome */
    @Override
    public boolean onCreateOptionsMenu(Menu menuItems) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_flightstatus_menu, menuItems);

        return true;
    }

    /** Selects the action that should happen when an item in the toolbar in selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flightStatusBtnReturn:
                // Snackbar to go back
                Snackbar snackbar = Snackbar.make(findViewById(R.id.flightStatusToolbarArrDep), "Are you sure?", Snackbar.LENGTH_LONG)
                        .setAction("Go Back!", e -> finish());
                snackbar.show();
                break;
            case R.id.flightStatusBtnHelp:
                // Author name in dialog box
                howToUseFlightTracker();
                break;
        }
        return true;
    }

    /** Displays the dialog with how to use app and author */
    public void howToUseFlightTracker(){
        View middle = getLayoutInflater().inflate(R.layout.flight_status_howtouse_author, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
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

}
