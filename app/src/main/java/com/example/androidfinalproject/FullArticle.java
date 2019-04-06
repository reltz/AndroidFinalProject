package com.example.androidfinalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class FullArticle extends AppCompatActivity {
    TextView title;
    TextView body;
    TextView link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        title=findViewById(R.id.news_title_detailed);
        body=findViewById(R.id.news_body);
        link = findViewById(R.id.nyArticleLink);

        Intent i = getIntent();
        String myTitle = i.getStringExtra("title");
        String myBody = i.getStringExtra("body");
        String linkText = i.getStringExtra("link");
        String imageLink = i.getStringExtra("imageLink");
        title.setText(myTitle);
        body.setText(myBody);
        link.setText(linkText);

        Toast.makeText(getApplicationContext(), R.string.nySaveAlert,
                Toast.LENGTH_LONG).show();
    }
}
