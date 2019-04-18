package com.example.androidfinalproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class FlightStatusSharedPreferences extends AppCompatActivity {
    android.content.SharedPreferences sp;
    EditText typeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_status_home);
//
        typeField = (EditText)findViewById(R.id.flightStatusSearchCode);
        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "Default value");

        typeField.setText(savedString);
    }
}
