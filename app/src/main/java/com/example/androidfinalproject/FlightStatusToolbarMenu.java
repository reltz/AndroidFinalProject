package com.example.androidfinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FlightStatusToolbarMenu extends AppCompatActivity {
    Toolbar flightStatusToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_toolbar_menu);

        flightStatusToolbar = findViewById(R.id.flightStatusToolbar);
        setSupportActionBar(flightStatusToolbar);
//        getSupportActionBar().setTitle("Flight Status Tracker");

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
                Snackbar snackbar = Snackbar.make(findViewById(R.id.flightStatusToolbarHome), "Are you sure?", Snackbar.LENGTH_LONG)
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
