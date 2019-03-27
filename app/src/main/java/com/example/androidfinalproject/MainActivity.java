package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button newsFeedBtn;
    android.support.v7.widget.Toolbar my_Toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsFeedBtn = findViewById(R.id.newsFeedBtn);

        my_Toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(my_Toolbar);

        newsFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextPage = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(nextPage);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextPage;
        switch(item.getItemId()){

            case R.id.newsFeedMenu:
                nextPage = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(nextPage);
                break;
        }
        return true;
    }
}
