package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class FullArticle extends AppCompatActivity {
    TextView title;
    TextView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        title=findViewById(R.id.news_title_detailed);
        body=findViewById(R.id.news_body);

        Intent i = getIntent();
        String myTitle = i.getStringExtra("title");
        String myBody = i.getStringExtra("body");
        title.setText(myTitle);
        body.setText(myBody);

        Toast.makeText(getApplicationContext(), R.string.nySaveAlert,
                Toast.LENGTH_LONG).show();
    }
}
