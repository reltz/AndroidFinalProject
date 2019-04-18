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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlightStatusHome extends FlightStatusToolbarMenu {
    public static final String ITEM_SELECTED = "FlightNumber";
    public static final String ITEM_STATUS = "FlightStatus";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ALTITUDE = "FlightAltitude";
    public static final String ITEM_SPEEDH = "FlightSpeedH";
    public static final String ITEM_SPEEDG = "FlightSpeedG";
    public static final String ITEM_SPEEDV = "FlightSpeedV";
    public static final String ITEM_ID = "ID";
    public static final int FLIGHT_DETAIL_ACTIVITY = 345;
    public ListView savedFlights;
    public static List<Flight> flights;
    SharedPreferences sp;
    EditText searchCodeSharedPref;
    int checkedTrackingMode;

    private ProgressBar progressBar;
    Toolbar toolbar;

    public static ListAdapter aListAdapter;

    Handler handler = null;

    boolean isAirport = false;

    /** FlightStatusHome onCreate -> Retrieves and displays layout*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_home);

        flights = new ArrayList<>();

        toolbar = findViewById(R.id.flightStatusToolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flight Status Tracker Home");

        boolean isTablet = findViewById(R.id.flightStatusDetailFragment) != null; //check if the FrameLayout is loaded

        //Shared preferences
        sp = getSharedPreferences("SearchCodesFile", Context.MODE_PRIVATE);
        searchCodeSharedPref = findViewById(R.id.flightStatusSearchCode);
        String savedCode = sp.getString("SearchCode", "");
        searchCodeSharedPref.setText(savedCode);

        Button searchFlights = findViewById(R.id.flightStatusSearchBtn);

        searchFlights.setOnClickListener(click -> {
            RadioGroup flightStatusRadioAirportFlight = findViewById(R.id.flightStatusRadioAirportFlight);
            checkedTrackingMode = flightStatusRadioAirportFlight.getCheckedRadioButtonId();
            EditText searchCode = findViewById(R.id.flightStatusSearchCode);

            /** Verifies which Radio Button has been checked */
            switch (checkedTrackingMode) {
                case R.id.flightStatusRadioAirportCode:
                    if (searchCode.getText().toString().equals("")) {
                        Toast.makeText(this, "Please type the airport code", Toast.LENGTH_LONG).show();
                        break;
                    }

                    isAirport = true;

                    searchByDialog();
                    break;
                case R.id.flightStatusRadioFlightCode:
                    if (searchCode.getText().toString().equals("")) {
                        Toast.makeText(this, "Please type the flight number", Toast.LENGTH_LONG).show();
                        break;
                    }

                    isAirport = false;

                    handler = new Handler(msg -> {
                        if(msg.what == 0){
                            Toast.makeText(getApplicationContext(), "Flight Not Found", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    });

                    // Create JSON Array
                    //String text = "[{\"geography\":{\"latitude\":35.6035,\"longitude\":-81.0245,\"altitude\":5045.73,\"direction\":2.54855},\"speed\":{\"horizontal\":668.211,\"isGround\":0,\"vertical\":0},\"departure\":{\"iataCode\":\"CLT\",\"icaoCode\":\"KCLT\"},\"arrival\":{\"iataCode\":\"ROA\",\"icaoCode\":\"KROA\"},\"aircraft\":{\"regNumber\":\"N692AE\",\"icaoCode\":\"E145\",\"icao24\":\"\",\"iataCode\":\"E145\"},\"airline\":{\"iataCode\":\"PT\",\"icaoCode\":\"PDT\"},\"flight\":{\"iataNumber\":\"PT4958\",\"icaoNumber\":\"PDT4958\",\"number\":\"4958\"},\"system\":{\"updated\":\"1554164459\",\"squawk\":\"7164\"},\"status\":\"en-route\"},{\"geography\":{\"latitude\":34.5448,\"longitude\":-79.5638,\"altitude\":4176.83,\"direction\":176.404},\"speed\":{\"horizontal\":658.586,\"isGround\":0,\"vertical\":0},\"departure\":{\"iataCode\":\"PGV\",\"icaoCode\":\"KPGV\"},\"arrival\":{\"iataCode\":\"CLT\",\"icaoCode\":\"KCLT\"},\"aircraft\":{\"regNumber\":\"N669MB\",\"icaoCode\":\"\",\"icao24\":\"\",\"iataCode\":\"\"},\"airline\":{\"iataCode\":\"PT\",\"icaoCode\":\"PDT\"},\"flight\":{\"iataNumber\":\"PT4960\",\"icaoNumber\":\"PDT4960\",\"number\":\"4960\"},\"system\":{\"updated\":\"1554164459\",\"squawk\":\"6502\"},\"status\":\"en-route\"},{\"geography\":{\"latitude\":37.5041,\"longitude\":-122.423,\"altitude\":3262.2,\"direction\":45},\"speed\":{\"horizontal\":608.424,\"isGround\":0,\"vertical\":0},\"departure\":{\"iataCode\":\"MSP\",\"icaoCode\":\"KMSP\"},\"arrival\":{\"iataCode\":\"LAX\",\"icaoCode\":\"KLAX\"},\"aircraft\":{\"regNumber\":\"N986SW\",\"icaoCode\":\"CRJ2\",\"icao24\":\"\",\"iataCode\":\"CRJ2\"},\"airline\":{\"iataCode\":\"OO\",\"icaoCode\":\"SKW\"},\"flight\":{\"iataNumber\":\"OO5425\",\"icaoNumber\":\"SKW5425\",\"number\":\"5425\"},\"system\":{\"updated\":\"1554164459\",\"squawk\":\"3635\"},\"status\":\"started\"},{\"geography\":{\"latitude\":15.1988,\"longitude\":-23.3615,\"altitude\":3361.28,\"direction\":200},\"speed\":{\"horizontal\":574.921,\"isGround\":1,\"vertical\":0},\"departure\":{\"iataCode\":\"LIS\",\"icaoCode\":\"LPPT\"},\"arrival\":{\"iataCode\":\"RAI\",\"icaoCode\":\"GVFM\"},\"aircraft\":{\"regNumber\":\"CSTTN\",\"icaoCode\":\"A319\",\"icao24\":\"49528E\",\"iataCode\":\"A319\"},\"airline\":{\"iataCode\":\"TP\",\"icaoCode\":\"TAP\"},\"flight\":{\"iataNumber\":\"TP1543\",\"icaoNumber\":\"TAP1543\",\"number\":\"1543\"},\"system\":{\"updated\":\"1554164459\",\"squawk\":\"0\"},\"status\":\"en-route\"},{\"geography\":{\"latitude\":25.4946,\"longitude\":121.582,\"altitude\":4169.21,\"direction\":185},\"speed\":{\"horizontal\":629.34,\"isGround\":0,\"vertical\":0},\"departure\":{\"iataCode\":\"TPE\",\"icaoCode\":\"TPE\"},\"arrival\":{\"iataCode\":\"ICN\",\"icaoCode\":\"ICN\"},\"aircraft\":{\"regNumber\":\"B16209\",\"icaoCode\":\"A321\",\"icao24\":\"89904F\",\"iataCode\":\"A321\"},\"airline\":{\"iataCode\":\"B7\",\"icaoCode\":\"UIA\"},\"flight\":{\"iataNumber\":\"B7170\",\"icaoNumber\":\"UIA170\",\"number\":\"170\"},\"system\":{\"updated\":\"1554164459\",\"squawk\":\"6263\"},\"status\":\"started\"}]";

                    FlightStatusQuery networkThread = new FlightStatusQuery();
                    networkThread.execute("http://torunski.ca/flights.json");

//                    JSONArray jArray = null;
//                    try {
//                        jArray = new JSONArray(text);
//
//                        // Create JSON object
//                        JSONObject flight1Json = null;
//                        flight1Json = new JSONObject(jArray.getJSONObject(0).toString());
//                        JSONObject flight2Json = new JSONObject(jArray.getJSONObject(1).toString());
//                        JSONObject flight3Json = new JSONObject(jArray.getJSONObject(2).toString());
//                        JSONObject flight4Json = new JSONObject(jArray.getJSONObject(3).toString());
//                        JSONObject flight5Json = new JSONObject(jArray.getJSONObject(4).toString());
//
//                        String flight1Number = "AC090";
////                        String flight1Number = flight1Json.getJSONObject("flight").getString("iataNumber");
//                        String flight1Status = "";
//                        String flight1DepartureFrom = "";
//                        String flight1ArrivalAt = "";
//
//    //                    publishProgress(50);
//
//                        Bundle dataFromWeb = null;
//
//                        if(searchCode.getText().toString().equalsIgnoreCase(flight1Number)){
//                            flight1Status = flight1Json.getString("status");
//                            flight1DepartureFrom = flight1Json.getJSONObject("departure").getString("iataCode");
//                            flight1ArrivalAt = flight1Json.getJSONObject("arrival").getString("iataCode");
//
//                            // take data
//                            dataFromWeb = new Bundle();
//                            dataFromWeb.putString(ITEM_SELECTED, flight1Number);
//                            dataFromWeb.putString(ITEM_STATUS, flight1Status);
//                            dataFromWeb.putString("FlightDeparture", flight1DepartureFrom);
//                            dataFromWeb.putString("FlightArrival", flight1ArrivalAt);
//
//                            if(isTablet){
//                                FlightStatusFlightInformationFragment dFragment = new FlightStatusFlightInformationFragment(); //add a DetailFragment
//                                dFragment.setArguments( dataFromWeb ); //pass it a bundle for information
//                                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .add(R.id.flightStatusFragment, dFragment) //Add the fragment in FrameLayout
//                                        .addToBackStack("Flight Details") //make the back button undo the transaction
//                                        .commit(); //actually load the fragment.
//
//                            } else {
//                                Intent flightInformation = new Intent(FlightStatusHome.this, FlightStatusFlightInformationEmpty.class);
//                                flightInformation.putExtras(dataFromWeb);
//                                startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);
//                            }
//    //                        publishProgress(75);
//                        } else {
//                            handler.sendEmptyMessage(0);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    break;
                default:
                    Toast.makeText(this, "Please select an Option", Toast.LENGTH_LONG).show();
                    break;
            }
        });

        //get a database:
        FlightStatusDbOpen dbOpener = new FlightStatusDbOpen(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

//        dbOpener.dropTable(db);
        //dbOpener.onUpgrade(db, 2, 3);

        //query all the results from the database:
        String [] columns = {FlightStatusDbOpen.COL_ID, FlightStatusDbOpen.COL_NUMBER, FlightStatusDbOpen.COL_STATUS, FlightStatusDbOpen.COL_SPEEDHORIZONTAL, FlightStatusDbOpen.COL_SPEEDISGROUND, FlightStatusDbOpen.COL_SPEEDVERTICAL, FlightStatusDbOpen.COL_ALTITUDE};
        Cursor results = db.query(false, FlightStatusDbOpen.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int idColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_ID);
        int numberColumnIndex = results.getColumnIndex(FlightStatusDbOpen.COL_NUMBER);
        int statusColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_STATUS);
        int speedHColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_SPEEDHORIZONTAL);
        int speedGColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_SPEEDISGROUND);
        int speedVColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_SPEEDVERTICAL);
        int altitudeColIndex = results.getColumnIndex(FlightStatusDbOpen.COL_ALTITUDE);

        //iterate over the results, returnbtn true if there is a next item:
        while(results.moveToNext()){
            int id = results.getInt(idColIndex);
            String flightNumber = results.getString(numberColumnIndex);
            String status = results.getString(statusColIndex);
            String speedH = results.getString(speedHColIndex);
            boolean speedG = results.getInt(speedGColIndex) != 0;
            String speedV = results.getString(speedVColIndex);
            String altitude = results.getString(altitudeColIndex);

            db.close();
            dbOpener.close();
            //add the new Flight to the array list:
            flights.add(new Flight(id, flightNumber, status, speedH, speedG, speedV, altitude));
        }

        /** Inflate ListView with flights previously saved by user */
        savedFlights = findViewById(R.id.flightStatusSavedFlights);
//        Flight flight = new Flight("AC090", "en-route");
//        Flight flight2 = new Flight("AC091", "arriving");
//        Flight flight3 = new Flight("AC092", "landed");

//        flights.add(flight);
//        flights.add(flight2);
//        flights.add(flight3);

        aListAdapter = new ListAdapter(flights, getApplicationContext());
        savedFlights.setAdapter(aListAdapter);

//        ProgressBar progressBar = findViewById(R.id.flightProgressBar);
//        progressBar.setVisibility(View.VISIBLE);

        savedFlights.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putInt(ITEM_ID, flights.get(position).getId());
            dataToPass.putString(ITEM_SELECTED, flights.get(position).getFlightNumber());
            dataToPass.putString(ITEM_STATUS, flights.get(position).getFlightStatus());
            dataToPass.putString(ITEM_SPEEDH, flights.get(position).getFlightSpeedHorizontal());
            dataToPass.putString(ITEM_SPEEDG, String.valueOf(flights.get(position).getFlightSpeedIsGround()));
            dataToPass.putString(ITEM_SPEEDV, flights.get(position).getFlightSpeedVertical());
            dataToPass.putString(ITEM_ALTITUDE, flights.get(position).getFlightAltitude());

            if(isTablet){
                FlightStatusFlightInformationFragment dFragment = new FlightStatusFlightInformationFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.flightStatusDetailFragment, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("Flight Details") //make the back button undo the transaction
                        .commit(); //actually load the fragment.

            } else {
                Intent flightInformation = new Intent(FlightStatusHome.this, FlightStatusFlightInformationEmpty.class);
                flightInformation.putExtras(dataToPass);
                startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);
            }
        });

        aListAdapter.notifyDataSetChanged();

        progressBar = findViewById(R.id.flightStatusInfoProgressBar);
//        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String spSearchCode = searchCodeSharedPref.getText().toString();
        editor.putString("SearchCode", spSearchCode);

        //write it to disk:
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FLIGHT_DETAIL_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
//                if(data.getIntExtra("position", 0)
                long id = data.getLongExtra(ITEM_ID, 0);
                String flightNum = data.getStringExtra("FlightNum");

//                flights.remove(id);
                if(data.getStringExtra("SaveOrDelete").equalsIgnoreCase("Save")){
                    // Save
                    String x = "Gustavo";
                } else{
                    FlightStatusDbOpen dbOpener = new FlightStatusDbOpen(this);
                    SQLiteDatabase db = dbOpener.getWritableDatabase();

                    db.delete(FlightStatusDbOpen.TABLE_NAME, FlightStatusDbOpen.COL_NUMBER + "=?", new String[]{flightNum});

                    int position = data.getIntExtra("position", 0);
                    flights.remove(position);
                    aListAdapter.notifyDataSetChanged();
                    db.close();
                    dbOpener.close();
                }
            }
        }
    }

    /** Opens dialog for the user to select Arrivals or Departures at the searched airport */
    public void searchByDialog(){
        View middle = getLayoutInflater().inflate(R.layout.searchbyairport, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ImageButton arrivals = middle.findViewById(R.id.arrivalsImgBtn);
        arrivals.setOnClickListener(click -> {
            Toast.makeText(this, "Arrivals clicked", Toast.LENGTH_LONG).show();

            FlightStatusQuery networkThread = new FlightStatusQuery();
            networkThread.execute("http://torunski.ca/flights.json");

            handler = new Handler(msg -> {
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), "No Airport Found", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        });

        ImageButton departures = middle.findViewById(R.id.departuresImgbtn);
        departures.setOnClickListener(click -> {
            Toast.makeText(this, "Departures clicked", Toast.LENGTH_LONG).show();

            FlightStatusQuery networkThread = new FlightStatusQuery();
            networkThread.execute("torunski.ca/flights.json");

            handler = new Handler(msg -> {
                if(msg.what == 0){
                    Toast.makeText(getApplicationContext(), "No Airport Found", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
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

    /** Deletes a flight from saved list */
    public void updateFlightListView(View view){
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
//                URL url = new URL(flightStatusUrl);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//
//                // json is UTF-8 by default
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//                StringBuilder sb = new StringBuilder();
//
//                String line = null;
//                while ((line = reader.readLine()) != null)
//                {
//                    sb.append(line + "\n");
//                }
                String result = "[\n" +
                        "    {\n" +
                        "        \"geography\": {\n" +
                        "            \"latitude\": 35.6035,\n" +
                        "            \"longitude\": -81.0245,\n" +
                        "            \"altitude\": 5045.73,\n" +
                        "            \"direction\": 2.54855\n" +
                        "        },\n" +
                        "        \"speed\": {\n" +
                        "            \"horizontal\": 668.211,\n" +
                        "            \"isGround\": 0,\n" +
                        "            \"vertical\": 0\n" +
                        "        },\n" +
                        "        \"departure\": {\n" +
                        "            \"iataCode\": \"CLT\",\n" +
                        "            \"icaoCode\": \"KCLT\"\n" +
                        "        },\n" +
                        "        \"arrival\": {\n" +
                        "            \"iataCode\": \"ROA\",\n" +
                        "            \"icaoCode\": \"KROA\"\n" +
                        "        },\n" +
                        "        \"aircraft\": {\n" +
                        "            \"regNumber\": \"N692AE\",\n" +
                        "            \"icaoCode\": \"E145\",\n" +
                        "            \"icao24\": \"\",\n" +
                        "            \"iataCode\": \"E145\"\n" +
                        "        },\n" +
                        "        \"airline\": {\n" +
                        "            \"iataCode\": \"PT\",\n" +
                        "            \"icaoCode\": \"PDT\"\n" +
                        "        },\n" +
                        "        \"flight\": {\n" +
                        "            \"iataNumber\": \"PT4958\",\n" +
                        "            \"icaoNumber\": \"PDT4958\",\n" +
                        "            \"number\": \"4958\"\n" +
                        "        },\n" +
                        "        \"system\": {\n" +
                        "            \"updated\": \"1554164459\",\n" +
                        "            \"squawk\": \"7164\"\n" +
                        "        },\n" +
                        "        \"status\": \"en-route\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"geography\": {\n" +
                        "            \"latitude\": 34.5448,\n" +
                        "            \"longitude\": -79.5638,\n" +
                        "            \"altitude\": 4176.83,\n" +
                        "            \"direction\": 176.404\n" +
                        "        },\n" +
                        "        \"speed\": {\n" +
                        "            \"horizontal\": 658.586,\n" +
                        "            \"isGround\": 0,\n" +
                        "            \"vertical\": 0\n" +
                        "        },\n" +
                        "        \"departure\": {\n" +
                        "            \"iataCode\": \"PGV\",\n" +
                        "            \"icaoCode\": \"KPGV\"\n" +
                        "        },\n" +
                        "        \"arrival\": {\n" +
                        "            \"iataCode\": \"CLT\",\n" +
                        "            \"icaoCode\": \"KCLT\"\n" +
                        "        },\n" +
                        "        \"aircraft\": {\n" +
                        "            \"regNumber\": \"N669MB\",\n" +
                        "            \"icaoCode\": \"\",\n" +
                        "            \"icao24\": \"\",\n" +
                        "            \"iataCode\": \"\"\n" +
                        "        },\n" +
                        "        \"airline\": {\n" +
                        "            \"iataCode\": \"PT\",\n" +
                        "            \"icaoCode\": \"PDT\"\n" +
                        "        },\n" +
                        "        \"flight\": {\n" +
                        "            \"iataNumber\": \"PT4960\",\n" +
                        "            \"icaoNumber\": \"PDT4960\",\n" +
                        "            \"number\": \"4960\"\n" +
                        "        },\n" +
                        "        \"system\": {\n" +
                        "            \"updated\": \"1554164459\",\n" +
                        "            \"squawk\": \"6502\"\n" +
                        "        },\n" +
                        "        \"status\": \"en-route\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"geography\": {\n" +
                        "            \"latitude\": 37.5041,\n" +
                        "            \"longitude\": -122.423,\n" +
                        "            \"altitude\": 3262.2,\n" +
                        "            \"direction\": 45\n" +
                        "        },\n" +
                        "        \"speed\": {\n" +
                        "            \"horizontal\": 608.424,\n" +
                        "            \"isGround\": 0,\n" +
                        "            \"vertical\": 0\n" +
                        "        },\n" +
                        "        \"departure\": {\n" +
                        "            \"iataCode\": \"MSP\",\n" +
                        "            \"icaoCode\": \"KMSP\"\n" +
                        "        },\n" +
                        "        \"arrival\": {\n" +
                        "            \"iataCode\": \"LAX\",\n" +
                        "            \"icaoCode\": \"KLAX\"\n" +
                        "        },\n" +
                        "        \"aircraft\": {\n" +
                        "            \"regNumber\": \"N986SW\",\n" +
                        "            \"icaoCode\": \"CRJ2\",\n" +
                        "            \"icao24\": \"\",\n" +
                        "            \"iataCode\": \"CRJ2\"\n" +
                        "        },\n" +
                        "        \"airline\": {\n" +
                        "            \"iataCode\": \"OO\",\n" +
                        "            \"icaoCode\": \"SKW\"\n" +
                        "        },\n" +
                        "        \"flight\": {\n" +
                        "            \"iataNumber\": \"OO5425\",\n" +
                        "            \"icaoNumber\": \"SKW5425\",\n" +
                        "            \"number\": \"5425\"\n" +
                        "        },\n" +
                        "        \"system\": {\n" +
                        "            \"updated\": \"1554164459\",\n" +
                        "            \"squawk\": \"3635\"\n" +
                        "        },\n" +
                        "        \"status\": \"started\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"geography\": {\n" +
                        "            \"latitude\": 15.1988,\n" +
                        "            \"longitude\": -23.3615,\n" +
                        "            \"altitude\": 3361.28,\n" +
                        "            \"direction\": 200\n" +
                        "        },\n" +
                        "        \"speed\": {\n" +
                        "            \"horizontal\": 574.921,\n" +
                        "            \"isGround\": 1,\n" +
                        "            \"vertical\": 0\n" +
                        "        },\n" +
                        "        \"departure\": {\n" +
                        "            \"iataCode\": \"LIS\",\n" +
                        "            \"icaoCode\": \"LPPT\"\n" +
                        "        },\n" +
                        "        \"arrival\": {\n" +
                        "            \"iataCode\": \"RAI\",\n" +
                        "            \"icaoCode\": \"GVFM\"\n" +
                        "        },\n" +
                        "        \"aircraft\": {\n" +
                        "            \"regNumber\": \"CSTTN\",\n" +
                        "            \"icaoCode\": \"A319\",\n" +
                        "            \"icao24\": \"49528E\",\n" +
                        "            \"iataCode\": \"A319\"\n" +
                        "        },\n" +
                        "        \"airline\": {\n" +
                        "            \"iataCode\": \"TP\",\n" +
                        "            \"icaoCode\": \"TAP\"\n" +
                        "        },\n" +
                        "        \"flight\": {\n" +
                        "            \"iataNumber\": \"TP1543\",\n" +
                        "            \"icaoNumber\": \"TAP1543\",\n" +
                        "            \"number\": \"1543\"\n" +
                        "        },\n" +
                        "        \"system\": {\n" +
                        "            \"updated\": \"1554164459\",\n" +
                        "            \"squawk\": \"0\"\n" +
                        "        },\n" +
                        "        \"status\": \"en-route\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"geography\": {\n" +
                        "            \"latitude\": 25.4946,\n" +
                        "            \"longitude\": 121.582,\n" +
                        "            \"altitude\": 4169.21,\n" +
                        "            \"direction\": 185\n" +
                        "        },\n" +
                        "        \"speed\": {\n" +
                        "            \"horizontal\": 629.34,\n" +
                        "            \"isGround\": 0,\n" +
                        "            \"vertical\": 0\n" +
                        "        },\n" +
                        "        \"departure\": {\n" +
                        "            \"iataCode\": \"TPE\",\n" +
                        "            \"icaoCode\": \"TPE\"\n" +
                        "        },\n" +
                        "        \"arrival\": {\n" +
                        "            \"iataCode\": \"ICN\",\n" +
                        "            \"icaoCode\": \"ICN\"\n" +
                        "        },\n" +
                        "        \"aircraft\": {\n" +
                        "            \"regNumber\": \"B16209\",\n" +
                        "            \"icaoCode\": \"A321\",\n" +
                        "            \"icao24\": \"89904F\",\n" +
                        "            \"iataCode\": \"A321\"\n" +
                        "        },\n" +
                        "        \"airline\": {\n" +
                        "            \"iataCode\": \"B7\",\n" +
                        "            \"icaoCode\": \"UIA\"\n" +
                        "        },\n" +
                        "        \"flight\": {\n" +
                        "            \"iataNumber\": \"B7170\",\n" +
                        "            \"icaoNumber\": \"UIA170\",\n" +
                        "            \"number\": \"170\"\n" +
                        "        },\n" +
                        "        \"system\": {\n" +
                        "            \"updated\": \"1554164459\",\n" +
                        "            \"squawk\": \"6263\"\n" +
                        "        },\n" +
                        "        \"status\": \"started\"\n" +
                        "    }\n" +
                        "]\n";

                EditText searchCode = findViewById(R.id.flightStatusSearchCode);
                String searchCodeText = searchCode.getText().toString();

                JSONArray jArray = new JSONArray(result);

                ArrayList<JSONObject> flight = new ArrayList<>(jArray.length());

//                for(int i = 0; i < flights.length; i++){
//                    flights[i] = new JSONObject(jArray.getJSONObject(i).toString());
//                }

                String flight1Number = "";
                String flight1Status = "";
                String flight1DepartureFrom = "";
                String flight1ArrivalAt = "";

                publishProgress(50);

                Bundle dataFromWeb = null;
                boolean found = false;



                for(int i = 0; i < jArray.length(); i++) {
                    if (searchCode.getText().toString().equalsIgnoreCase(jArray.getJSONObject(i).getJSONObject("arrival").getString("iataCode")) && isAirport) {
                        found = true;
                        flight.add(jArray.getJSONObject(i));

//                        flight1Number = flights[i].getJSONObject("flight").getString("iataNumber");
//                        flight1Status = flights[i].getString("status");
//                        String flight1SpeedH = flights[i].getJSONObject("speed").getString("horizontal");
//                        String flight1SpeedG = flights[i].getJSONObject("speed").getString("isGround");
//                        String flight1SpeedV = flights[i].getJSONObject("speed").getString("vertical");
//                        String flight1Altitude = flights[i].getJSONObject("geography").getString("altitude");
////                      flight1DepartureFrom = flights[i].getJSONObject("departure").getString("iataCode");
////                      flight1ArrivalAt = flights[i].getJSONObject("arrival").getString("iataCode");
//
//                        // take data
//                        dataFromWeb = new Bundle();
//                        dataFromWeb.putString("FlightNumber", flight1Number);
//                        dataFromWeb.putString("FlightStatus", flight1Status);
//                        dataFromWeb.putString("FlightSpeedH", flight1SpeedH);
//                        dataFromWeb.putString("FlightSpeedG", flight1SpeedG);
//                        dataFromWeb.putString("FlightSpeedV", flight1SpeedV);
//                        dataFromWeb.putString("FlightAltitude", flight1Altitude);

                        Intent intent = new Intent(FlightStatusHome.this, FlightStatusArrivalsInformation.class);
                        intent.putExtra("arrivals_list", flight.toString());
//                        intent.putExtras(dataFromWeb);

                        publishProgress(75);

                        startActivityForResult(intent, 345);

                        break;
                    } else if(searchCode.getText().toString().equalsIgnoreCase(jArray.getJSONObject(i).getJSONObject("departure").getString("iataCode")) && isAirport){
                        found = true;
                        Intent intent = new Intent(FlightStatusHome.this, FlightStatusArrivalsInformation.class);
                        intent.putExtra("arrivals_list", jArray.toString());
                    } else if(searchCode.getText().toString().equalsIgnoreCase(jArray.getJSONObject(i).getJSONObject("flight").getString("iataNumber"))){
                       // Toast.makeText(getApplicationContext(), "No Airport found, but Flight FOUND", Toast.LENGTH_LONG).show();
                        found = true;
                        flight1Number = jArray.getJSONObject(i).getJSONObject("flight").getString("iataNumber");
                        flight1Status = jArray.getJSONObject(i).getString("status");
                        String flight1SpeedH = jArray.getJSONObject(i).getJSONObject("speed").getString("horizontal");
                        String flight1SpeedG = jArray.getJSONObject(i).getJSONObject("speed").getString("isGround");
                        String flight1SpeedV = jArray.getJSONObject(i).getJSONObject("speed").getString("vertical");
                        String flight1Altitude = jArray.getJSONObject(i).getJSONObject("geography").getString("altitude");
//                      flight1DepartureFrom = flights[i].getJSONObject("departure").getString("iataCode");
//                      flight1ArrivalAt = flights[i].getJSONObject("arrival").getString("iataCode");

                        // take data
                        dataFromWeb = new Bundle();
                        dataFromWeb.putString("FlightNumber", flight1Number);
                        dataFromWeb.putString("FlightStatus", flight1Status);
                        dataFromWeb.putString("FlightSpeedH", flight1SpeedH);
                        dataFromWeb.putString("FlightSpeedG", flight1SpeedG);
                        dataFromWeb.putString("FlightSpeedV", flight1SpeedV);
                        dataFromWeb.putString("FlightAltitude", flight1Altitude);

                        Intent flightInformation = new Intent(FlightStatusHome.this, FlightStatusFlightInformationEmpty.class);
                        flightInformation.putExtras(dataFromWeb);
                        startActivityForResult(flightInformation, FLIGHT_DETAIL_ACTIVITY);
                    }

                    if(found){
                        break;
                    }
                }
                if(!found){
                    handler.sendEmptyMessage(0);
                }

                // urlConnection.disconnect();

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

    public static void updateFlightListView(){
        aListAdapter.notifyDataSetChanged();
    }

}

