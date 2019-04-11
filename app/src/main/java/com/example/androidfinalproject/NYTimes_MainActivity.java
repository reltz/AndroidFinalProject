package com.example.androidfinalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that defines the Main activity for the New York Times Feed
 * Author: Rodrigo Eltz
 * Date: April 2019
 * Version: 2.0
 **/
public class NYTimes_MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private Button goBack;
    private Button search;
    private EditText typeSearch;
    private ListView nyFeed;
    private ProgressBar progress;
    private List<Article> newsList;
    private Toolbar helpBar;

//NY times milestone 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_main);
        goBack = findViewById(R.id.nyBackButton);
        search = findViewById(R.id.nySearchButton);
        nyFeed = findViewById(R.id.listView);
        progress = findViewById(R.id.indeterminateBar);
        helpBar = findViewById(R.id.nyToolbarHelp);
        typeSearch = findViewById(R.id.nyTypeSearch);
        setSupportActionBar(helpBar);
        sp = getSharedPreferences("fileName", Context.MODE_PRIVATE);
        this.loadSearchTerm();


        // Set progress bar to visible
        progress.setVisibility(View.VISIBLE);

        goBack.setOnClickListener(a -> {
            Snackbar sb = Snackbar.make(goBack, "Go Back?", Snackbar.LENGTH_LONG);
            sb.setAction("Confirm!", b -> finish());
            sb.show();
        });

        search.setOnClickListener(a -> {
            String term = typeSearch.getText().toString();
            Intent goSearch = new Intent(NYTimes_MainActivity.this, NYT_search_result.class);
            goSearch.putExtra("term", term);
            startActivity(goSearch);

        });


        newsList = new ArrayList<>();
        Log.e("status", "Created news array list");

        //newsList.add(new Article("test1", "empty", 0));
        DataFetcher networkThread = new DataFetcher();
        Log.e("status", "created datafetcher thread");
        //starts background thread
        networkThread.execute();
        Log.e("status", "executed thread");

        nyFeed.setClickable(true);
        nyFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent nextArticle = new Intent(NYTimes_MainActivity.this, FullArticle.class);
                nextArticle.putExtra("title", newsList.get(position).getTitle());
                nextArticle.putExtra("body", newsList.get(position).getBody());
                nextArticle.putExtra("link", newsList.get(position).getLink());
                nextArticle.putExtra("imageLink", newsList.get(position).getImageLink());

                startActivity(nextArticle);

            }
        });
    }


    /**
     * Override onPause with the saveSearchTerm method, which saves in shared preferences object the term that was searched last.
     */
    protected void onPause() {
        super.onPause();
        this.saveSearchTerm();
    }

    protected void saveSearchTerm() {
        //EditText term = findViewById(R.id.nyTypeSearch);
        String whatWasTyped = typeSearch.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("savedTerm", whatWasTyped);
        editor.commit();
    }

    /**
     * Method that loads the last search term to the editText widget.
     */
    protected void loadSearchTerm() {
        String saved = sp.getString("savedTerm", "");
        //EditText typeSearch = findViewById(R.id.nyTypeSearch);
        typeSearch.setText(saved);
    }


    /**
     * Methor that inflates the Menu for the toolbar
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
                Intent goSavedNow = new Intent(NYTimes_MainActivity.this, NYT_savedArticles.class);
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
     * Private class DataFetcher - retrieves data in json format from NYTimes web server api
     **/
    private class DataFetcher extends AsyncTask<String, Integer, String> {
        private List<Article> news = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {

            try {

                //JSON
                // URL
                URL jurl = new URL("https://api.nytimes.com/svc/news/v3/content/all/all.json?api-key=6Y1zFdkuCRAVyJAQBwhgB9x3Dgw1F5JA");
                HttpURLConnection jurlConnection = (HttpURLConnection) jurl.openConnection();
                InputStream inStream = jurlConnection.getInputStream();

                //create JSON object for response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                //now a json table
                JSONObject jObject = new JSONObject(result);
                JSONArray results = jObject.getJSONArray("results");
                for (int index = 0; index < results.length(); index++) {

                    //this check if multimedia array is there (some articles dont have images, or are not actual articles)
                    if (results.getJSONObject(index).has("multimedia") && results.getJSONObject(index).get("multimedia") instanceof JSONArray) {
                        news.add(new Article(results.getJSONObject(index).getString("title"),
                                results.getJSONObject(index).getString("abstract"), results.getJSONObject(index).getString("url"),
                                results.getJSONObject(index).getJSONArray("multimedia").getJSONObject(2).getString("url"), index));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("status:", "finished background");
            return "finished task";

        }

        /**
         * Method that adds all the articles found in the json from web server to the local arraylist of articles
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            for (Article a : news) {
                newsList.add(a);
                Log.e("status", "added item to arraylist news");

            }
            ArticleAdapter adapter = new ArticleAdapter(newsList, getApplicationContext());
            nyFeed.setAdapter(adapter);
            progress.setVisibility(View.INVISIBLE);
            Log.e("status", "Adapter set for listview");
            Log.e("status:", "done with postExecute");
        }
    }


}
