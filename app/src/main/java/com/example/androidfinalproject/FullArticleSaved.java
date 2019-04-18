package com.example.androidfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity that opens when a saved article is clicked from the saved articles listview
 * Difference from the FullArticle activity is that this has the button delete to remove article from database
 * @author Rodrigo Eltz
 * @since 10-04-2019
 */
public class FullArticleSaved extends AppCompatActivity {
    private TextView title;
    private TextView body;
    private TextView link;
    private ImageView image;
    private Toolbar helpBar;
    private NYT_DataBase db;
    private Button deleteArticle;
    public static final int REQUEST_CODE = 1;
    int deletedID=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nyt_fullarticle_saved);
        title = findViewById(R.id.news_title_detailed);
        body = findViewById(R.id.news_body);
        link = findViewById(R.id.nyArticleLink);
        image = findViewById(R.id.nyImageArticle);
        helpBar = findViewById(R.id.nyToolbarHelp2);
        setSupportActionBar(helpBar);
        deleteArticle = findViewById(R.id.nyButtonDelete);
        db = new NYT_DataBase(this);


        Intent i = getIntent();
        String myTitle = i.getStringExtra("title");
        String myBody = i.getStringExtra("body");
        String linkText = i.getStringExtra("link");
        String imageLink = i.getStringExtra("imageLink");
        int articleID = i.getIntExtra("id", -1);
        deletedID=articleID;



        title.setText(myTitle);
        body.setText(myBody);
        link.setText(linkText);


        if (!imageLink.equals("")) {
            DataFetcher networkThread = new DataFetcher();
            networkThread.execute(imageLink);
        }

        deleteArticle.setOnClickListener(b -> {
            try {
                boolean del = db.deleteData(articleID);

                if (del == true) {
                    Toast.makeText(getApplicationContext(), R.string.NYTdeleted,
                            Toast.LENGTH_LONG).show();
                    Log.e("status of db", "article deleted!");


                } else {
                    Log.e("status of db", "article was not deleted");
                }

                Intent intent = getIntent();
                intent.putExtra("deletedID", deletedID);
                setResult(RESULT_OK, intent);
                finish();


            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


    /** Inflates the menu for the toolbar
     *
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
                Intent goSavedNow = new Intent(FullArticleSaved.this, NYT_savedArticles.class);
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
     * Inner class that fetches and renders the BitMap image from the image link saved on the article
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
