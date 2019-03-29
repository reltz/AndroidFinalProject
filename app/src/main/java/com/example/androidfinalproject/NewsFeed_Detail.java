package com.example.androidfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NewsFeed_Detail extends AppCompatActivity {

    android.support.v7.widget.Toolbar my_Toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed__detail);

        my_Toolbar = findViewById(R.id.newsFeedDetail_toolbar);
        setSupportActionBar(my_Toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsfeed_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Add more Icons after
        switch(item.getItemId()){

            case R.id.newsFeedBack:
                Snackbar sb = Snackbar.make(my_Toolbar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                sb.show();
                break;
        }
        return true;
    }
}

