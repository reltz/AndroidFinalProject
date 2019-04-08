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
import android.widget.Button;
import android.widget.ListView;

public class NYT_savedArticles extends AppCompatActivity {
    private Toolbar helpBar;
    private Button goBack;
    private ListView savedArticles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyt_saved_articles);
        helpBar=findViewById(R.id.nyToolbarHelp3);
        setSupportActionBar(helpBar);

        goBack = findViewById(R.id.nytBack);

        goBack.setOnClickListener(b-> {
            Intent goFeed = new Intent(NYT_savedArticles.this, NYTimes_MainActivity.class);
            startActivity(goFeed);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.e("menu","got inflater");
        inflater.inflate(R.menu.nytimes_menu, menu);
        Log.e("menu","inflated menu!");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nytHelp:
                alertNytHelp();
                break;
        }
        return true;
    }

    /**
     * Method that creates a dialog box for when the menu help is clicked
     * Describes the author, version and instructions on how to use the app
     */
    public void alertNytHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nyAlertHelp).setPositiveButton(R.string.nyUnderstood, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });

        builder.create().show();
    }

}
