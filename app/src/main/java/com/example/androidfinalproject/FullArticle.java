package com.example.androidfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity that shows the Full Article, once it was clicked in the listview
 * @author Rodrigo Eltz
 * @since 10-04-2019
 */
public class FullArticle extends AppCompatActivity {
    private TextView title;
    private TextView body;
    private TextView link;
    private ImageView image;
    private Toolbar helpBar;
    private NYT_DataBase db;
    private Button saveArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        title = findViewById(R.id.news_title_detailed);
        body = findViewById(R.id.news_body);
        link = findViewById(R.id.nyArticleLink);
        image = findViewById(R.id.nyImageArticle);
        helpBar = findViewById(R.id.nyToolbarHelp2);
        setSupportActionBar(helpBar);
        saveArticle = findViewById(R.id.nyButtonSave);
        db = new NYT_DataBase(this);


        Bundle data = getIntent().getExtras();
        String myTitle = data.getString("title");

        String myBody = data.getString("body");
        String linkText = data.getString("link");
        String imageLink = data.getString("imageLink");
        title.setText(myTitle);
        body.setText(myBody);
        link.setText(linkText);


/**
 * Assync task to retrieve the Bitmap image from the image link saved on the object Article
 **/
        DataFetcher networkThread = new DataFetcher();
        networkThread.execute(imageLink);

        saveArticle.setOnClickListener(b-> {
            db.insertData(myTitle,myBody,linkText,imageLink);
            Log.e("db state","article saved!");

            Toast.makeText(getApplicationContext(), R.string.NYTsaved,
                    Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Method that inflates the Menu on the toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.e("menu", "got inflater");
        inflater.inflate(R.menu.nytimes_menu, menu);
        Log.e("menu", "inflated menu!");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nytHelp:
                alertNytHelp();
                break;
            case R.id.nytAllSaved:
                Intent goSavedNow = new Intent(FullArticle.this, NYT_savedArticles.class);
                startActivity(goSavedNow);
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

    /**
     * Inner class that fetches the Bitmap image from the image link and renders it
     */
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
