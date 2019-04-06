package com.example.androidfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FullArticle extends AppCompatActivity {
    TextView title;
    TextView body;
    TextView link;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        title=findViewById(R.id.news_title_detailed);
        body=findViewById(R.id.news_body);
        link = findViewById(R.id.nyArticleLink);
        image = findViewById(R.id.nyImageArticle);

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

        DataFetcher networkThread = new DataFetcher();
        networkThread.execute(imageLink);
    }

    private class DataFetcher extends AsyncTask<String, Integer, String> {
        private Bitmap myImage = null;
        private URL url = null;
        private HttpURLConnection connection = null;

        @Override
        protected String doInBackground(String... params) {
            String linkSource = params[0];
            try {
                url = new URL(linkSource);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int responseCode;
                responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    myImage = BitmapFactory.decodeStream(connection.getInputStream());
                                    }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "finished";
        }
        @Override
        protected void onPostExecute(String s) {
            image.setImageBitmap(myImage);

            }
        }



}
