package com.example.androidfinalproject;
/***********************************************************************************
 Class:    FlightStatusHome
 Purpose:  This activity will display the home page
 Author:   Gustavo Adami
 Course:   Android
 Data members:  ITEM_SELECTED : String, ITEM_POSITION : String, ITEM_ID : String, FLIGHT_DETAIL_ACTIVITY : String,
                flightStatusToolbar : Toolbar, savedFlights : ListView, flights : List<Flight>, progressBar : ProgressBar, handler : Handler
 Methods: onCreate(Bundle savedInstanceState) : void
 *************************************************************************************/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlightStatusHome extends AppCompatActivity {
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int FLIGHT_DETAIL_ACTIVITY = 345;
    Toolbar flightStatusToolbar;
    ListView savedFlights;
    List<Flight> flights = new ArrayList<>();

    private ProgressBar progressBar;

    Handler handler = null;

    /** FlightStatusHome onCreate -> Retrieves and displays layout*/
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
            EditText searchCode = findViewById(R.id.flightStatusSearchCode);

            /** Verifies which Radio Button has been checked */
            switch (checkedTrackingMode) {
                case R.id.flightStatusRadioAirportCode:
                    if (searchCode.getText().toString().equals("")) {
                        Toast.makeText(this, "Please type the airport code", Toast.LENGTH_LONG).show();
                        break;
                    }

                    searchByDialog();
                    break;
                case R.id.flightStatusRadioFlightCode:
                    if (searchCode.getText().toString().equals("")) {
                        Toast.makeText(this, "Please type the flight number", Toast.LENGTH_LONG).show();
                        break;
                    }

                    FlightStatusQuery networkThread = new FlightStatusQuery();
                    networkThread.execute("http://torunski.ca/flights.json");

//                    handler = new Handler() {
//                        public void handleMessage(Message msg){
//                            if(msg.what == 0)
//                                Toast.makeText(getApplicationContext(), "Flight Not Found", Toast.LENGTH_SHORT).show();
//                        }
//                    };

                    handler = new Handler(msg -> {
                        if(msg.what == 0){
                            Toast.makeText(getApplicationContext(), "Flight Not Found", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    });
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


//        ProgressBar progressBar = findViewById(R.id.flightProgressBar);
//        progressBar.setVisibility(View.VISIBLE);

        savedFlights.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, flights.get(position).getFlightName());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            Intent flightInformation = new Intent(FlightStatusHome.this, FlightStatusFlightInformation.class);
            flightInformation.putExtras(dataToPass);
            startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);

        });

        aListAdapter.notifyDataSetChanged();

        progressBar = findViewById(R.id.flightStatusInfoProgressBar);
//        progressBar.setVisibility(View.INVISIBLE);

    }

    /**
     * myButtonClickListener clicks the item in saved flights list
     */
    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);
        }
    };

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
                Snackbar snackbar = Snackbar.make(flightStatusToolbar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GO!", e -> finish());
                snackbar.show();
                break;
            case R.id.flightStatusBtnHelp:
                // Author name in dialog box
                howToUseFlightTracker();
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

    /** Displays flight info page */
    public void displayFlightInfo(View view){
//        for (int i=0; i < savedFlights.getChildCount(); i++){
//
//        }

        GridLayout flight = (GridLayout) view.getParent();

        Intent flightInformation = new Intent(getApplicationContext(), FlightStatusFlightInformation.class);
        startActivity(flightInformation);
    }

    /** Deletes a flight from saved list */
    public void deleteFlight(View view){
        Log.i("Delete flight", "deleting flight to be developed");
    }

    /** Adapter class for ListView saved flights */
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

    /** Class for internet connection and information retrieval */
    private class FlightStatusQuery extends AsyncTask<String, Integer, String>{


        @Override
        protected String doInBackground(String... params){
            try{
                String flightStatusUrl = params[0];

                publishProgress(25);

                //create the network connection:
                URL url = new URL(flightStatusUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                EditText searchCode = findViewById(R.id.flightStatusSearchCode);

                publishProgress(50);

                // Create JSON Array
                JSONArray jArray = new JSONArray(result);

                // Create JSON object
                JSONObject flight1Json = new JSONObject(jArray.getJSONObject(0).toString());
                JSONObject flight2Json = new JSONObject(jArray.getJSONObject(1).toString());
                JSONObject flight3Json = new JSONObject(jArray.getJSONObject(2).toString());
                JSONObject flight4Json = new JSONObject(jArray.getJSONObject(3).toString());
                JSONObject flight5Json = new JSONObject(jArray.getJSONObject(4).toString());

                publishProgress(75);

                String flight1Number = flight1Json.getJSONObject("flight").getString("iataNumber");
                String flight1Status = "";
                String flight1DepartureFrom = "";
                String flight1ArrivalAt = "";
                Bundle dataFromWeb = null;

                urlConnection.disconnect();



                if(searchCode.getText().toString().equalsIgnoreCase(flight1Number)){
                    flight1Status = flight1Json.getString("status");
                    flight1DepartureFrom = flight1Json.getJSONObject("departure").getString("iataCode");
                    flight1ArrivalAt = flight1Json.getJSONObject("arrival").getString("iataCode");

                    // take data
                    dataFromWeb = new Bundle();
                    dataFromWeb.putString("Flight Number", flight1Number);
                    dataFromWeb.putString("Flight Status", flight1Status);
                    dataFromWeb.putString("Flight Departure From", flight1DepartureFrom);
                    dataFromWeb.putString("Flight Arrival At", flight1ArrivalAt);

                    Intent intent = new Intent(FlightStatusHome.this, FlightStatusFlightInformation.class);
                    intent.putExtras(dataFromWeb);
                    startActivityForResult(intent, 345);

                } else {
                    handler.sendEmptyMessage(0);
                }

                publishProgress(100);
                Thread.sleep(2500);
            } catch (Exception ex){
                Log.e("JSON Crash :( !!", ex.getMessage());
            }

            return "ENDDD";
        }

        @Override
        public void onProgressUpdate(Integer ... values){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        public void onPostExecute(String s){
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

}

