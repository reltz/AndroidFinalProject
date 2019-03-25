package com.example.androidfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;

public class NYTimes_MainActivity extends AppCompatActivity {
    private Button goBack;
    private Button search;
    private EditText typeSearch;
    private ListView nyFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_main);
        goBack = findViewById(R.id.nyBackButton);
        typeSearch = findViewById(R.id.nyTypeSearch);
        search = findViewById(R.id.nySearchButton);
        nyFeed = findViewById(R.id.listView);


        goBack.setOnClickListener(a -> {
            Snackbar sb = Snackbar.make(goBack, "Go Back?", Snackbar.LENGTH_LONG);
            sb.setAction("Confirm!", b -> finish());
            sb.show();
        });


    }
}
