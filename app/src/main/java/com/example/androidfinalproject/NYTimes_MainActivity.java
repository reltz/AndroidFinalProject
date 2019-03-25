package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

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


        search.setOnClickListener(b -> {
            Intent searchList = new Intent(NYTimes_MainActivity.this, ArticleSearchList.class);
            startActivity(searchList);
        });


        List<Article> newsList = new ArrayList<Article>();
        newsList.add(new Article("Efforts to Legalize Marijuana in New Jersey Collapses", "TRENTON — A monthslong effort to legalize marijuana in New Jersey collapsed on Monday after Democrats were unable to muster enough support for the measure, rejecting a central campaign pledge from Gov. Philip D. Murphy and leaving the future of the legalization movement in doubt.\n" +
                "\n" +
                "The failure in the legislature marks one of the biggest setbacks for Mr. Murphy, who despite having full Democratic control in the State Senate and the assembly, had faced constant party infighting and had struggled to bend the legislature to his progressive agenda.\n" +
                "\n" +
                "[What you need to know to start the day: Get New York Today", 1));

        ArticleAdapter adapter = new ArticleAdapter(newsList,getApplicationContext());

        nyFeed.setAdapter(adapter);
    }
}
