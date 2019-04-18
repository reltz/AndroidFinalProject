package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsFeed_Search Class for the News Feed
 * @author Felipe Magnago
 * @since 17-04-2019
 * @version  1.0
 **/
public class NewsFeed_Search extends AppCompatActivity {
    private ListView newsLView;
    private ProgressBar SearchprogressBar;
    private List<NewsFeed> newsList;
    private NewsFeed_Database db;
    android.support.v7.widget.Toolbar my_Toolbar;
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_search);

        Intent current = getIntent();
        term = current.getStringExtra("searchTerm");
        Log.e("searchTerm",term);

        newsList = new ArrayList<>();
        SearchprogressBar = findViewById(R.id.SearchprogressBar);
        my_Toolbar = findViewById(R.id.newsFeed_toolbar);
        newsLView = findViewById(R.id.newsLView);
        db = new NewsFeed_Database(this);
        setSupportActionBar(my_Toolbar);

        //temporarelly invisible
        SearchprogressBar.setVisibility(View.VISIBLE);
        //FETCH DATA!
        DataFetcher networkThread = new DataFetcher();
        Log.e("status","created datafetcher thread");
        networkThread.execute();
        Log.e("status","executed thread");
        /**
         * Method that click on listview item and choose what to do
         *
         * @param parent
         * @param view
         * @param int
         * @param position
         * @param id
         * @return
         */
        newsLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_Search.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_choice, null);

                Button cancel = menuDetails.findViewById(R.id.newsCancelBtn);
                Button saveArticles = menuDetails.findViewById(R.id.saveNews);
                Button viewArticles = menuDetails.findViewById(R.id.openNews);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                viewArticles.setOnClickListener(c->{
                    String value = newsList.get(position).getURL();
                    Intent nextArticle = new Intent(NewsFeed_Search.this,NewsFeed_Detail.class);
                    nextArticle.putExtra("URL", value);
                    menuDialog.hide();
                    startActivity(nextArticle);
                });

                saveArticles.setOnClickListener(c->{
                    db.insertData(newsList.get(position).getNewsTitle(),newsList.get(position).getNewsBody(),newsList.get(position).getURL(), newsList.get(position).getImageURL());
                    Toast.makeText(NewsFeed_Search.this, "Article saved.", Toast.LENGTH_LONG).show();
                    menuDialog.hide();
                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
            }
        });
    }
    /**
     * Method that inflates the Menu for the toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsfeed_menu, menu);
        return true;
    }
    /**
     * Method that calls the handler for when the menu item is clicked
     *
     * @param item
     * @return
     */
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
            case R.id.newsFeedHelp:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.newsHelp).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().cancel();
                    }
                });
                builder.create().show();
                break;
            case R.id.newsFeedMenu:
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_Search.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_popup, null);

                Button cancel = menuDetails.findViewById(R.id.newsfeedPopUpCancelButton);

                Button viewSavedArticles = menuDetails.findViewById(R.id.goToSaved);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                viewSavedArticles.setOnClickListener(c->{
                        Intent intent = new Intent(NewsFeed_Search.this, NewsFeed_Saved.class);
                        menuDialog.hide();
                        startActivity(intent);
                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
        }
        return true;
    }
    /**
     * Private class DataFetcher - retrieves all typed search data in json
     * format from News Feed webhose api
     **/
    private class DataFetcher extends AsyncTask<String, Integer, String> {
        private List<NewsFeed> news = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {

            try {
                //JSON
                // URL
                String urlWithTerm = "http://webhose.io/filterWebContent?token=d6c018d7-dd06-4f40-94f0-ac6d291d3c5f&format=json&sort=crawled&q="+term;
                Log.e("JsonLink",urlWithTerm);
                URL jurl = new URL(urlWithTerm);
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
                JSONArray results = jObject.getJSONArray("posts");
                for (int index = 0; index < results.length(); index++) {

                    news.add(new NewsFeed(index, results.getJSONObject(index).getString("title"),
                            results.getJSONObject(index).getString("text"), results.getJSONObject(index).getString("url"),
                            null));
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
            for (NewsFeed a : news) {
                newsList.add(a);
                Log.e("status", "added item to arraylist news");

            }
            NewsFeed_Adapter adapter = new NewsFeed_Adapter(newsList, getApplicationContext());
            newsLView.setAdapter(adapter);
            SearchprogressBar.setVisibility(View.INVISIBLE);
            Log.e("status", "Adapter set for listview");
            Log.e("status:", "done with postExecute");
        }
    }
}
