package com.example.androidfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NYTimes_MainActivity extends AppCompatActivity {
    private Button goBack;
    private Button search;
    private EditText typeSearch;
    private ListView nyFeed;
    private ProgressBar progress;
    private Toolbar helpBar;

//NY times milestone 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_main);
        goBack = findViewById(R.id.nyBackButton);
        typeSearch = findViewById(R.id.nyTypeSearch);
        search = findViewById(R.id.nySearchButton);
        nyFeed = findViewById(R.id.listView);
        progress = findViewById(R.id.indeterminateBar);
        helpBar=findViewById(R.id.nyToolbarHelp);
        setSupportActionBar(helpBar);


        progress.setVisibility(View.VISIBLE);

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

        newsList.add(new Article("Title of second article", "This is the second article", 2));

        ArticleAdapter adapter = new ArticleAdapter(newsList, getApplicationContext());

        nyFeed.setClickable(true);


        nyFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent nextArticle = new Intent(NYTimes_MainActivity.this, FullArticle.class);
                    startActivity(nextArticle);
                }


            }
        });
        nyFeed.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nytimes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nytHelp:
                alertNytHelp();
                break;
        }
        return true;
    }

    public void alertNytHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Author: Rodrigo Eltz" +"\n"+"Version: 1.0"+
                "\n\n"+
                "Instructions: This app shows a list of latest New York Times Article." +
                " You can select one to read and save to your favorites, as well as search " +
                "for articles using the search box.").setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });

        builder.create().show();
    }
}
