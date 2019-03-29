package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed_MainActivity extends AppCompatActivity {

    EditText newsFeedET;
    Button newsFeedSearch;
    ProgressBar newsFeedPB;
    ListView newsFeedList;
    android.support.v7.widget.Toolbar my_Toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed__main);

        newsFeedList = findViewById(R.id.newsFeedList);
        newsFeedET = findViewById(R.id.newsFeedET);
        newsFeedPB = findViewById(R.id.newsFeedPB);
        newsFeedSearch = findViewById(R.id.newsFeedSearch);

        my_Toolbar = findViewById(R.id.newsFeed_toolbar);
        setSupportActionBar(my_Toolbar);

        final List<NewsFeed> newsList = new ArrayList<>();
        newsList.add(new NewsFeed(1, "News Feed 1", "Body of News Feed 1"));
        newsList.add(new NewsFeed(2, "News Feed 2", "Body of News Feed 2"));
        newsList.add(new NewsFeed(3, "News Feed 3", "Body of News Feed 3"));

        NewsFeed_Adapter adapter = new NewsFeed_Adapter(newsList, getApplicationContext());
        newsFeedList.setAdapter(adapter);

        newsFeedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;

                for (int i = 0; i < 3; i++) {
                    if (newsList.get(i).getNewsTitle().contains(newsFeedET.getText().toString())) {
                        counter++;
                    }
                }
                if (counter > 0) {
                    Toast.makeText(NewsFeed_MainActivity.this, "Found "+counter+" results for " + newsFeedET.getText().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewsFeed_MainActivity.this, "There is no newsfeed about this", Toast.LENGTH_LONG).show();
                }
                newsFeedPB.setVisibility(View.VISIBLE);
                newsFeedPB.setProgress(100);
                //newsFeedPB.setVisibility(View.INVISIBLE);
                //Thread.sleep(1500);
            }
        });

        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeed_MainActivity.this);
                builder.setMessage("Do you want to continue?");
                builder.setCancelable(true);
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent nextArticle = new Intent(NewsFeed_MainActivity.this,NewsFeed_Detail.class);
                        startActivity(nextArticle);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

//        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeed_MainActivity.this);
//                @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.news_feed_dialog, null);
//
//                TextView newsItem = details.findViewById(R.id.dialogNewsItem);
//                newsItem.setText("You clicked on " + newsList.get(position).getNewsTitle());
//
//                builder.setView(details);
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });

//        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent nextArticle = new Intent(NewsFeed_MainActivity.this,NewsFeed_Detail.class);
//                    startActivity(nextArticle);
//            }
//        });
    }

}
