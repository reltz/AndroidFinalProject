package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class NewsFeed_MainActivity extends AppCompatActivity {

    EditText newsFeedET;
    Button newsFeedSearch;
    ProgressBar newsFeedPB;
    ListView newsFeedList;
    NewsFeed_Database db;
    private List<NewsFeed> newsList;
    SharedPreferences sp;
    private boolean isTablet;
    android.support.v7.widget.Toolbar my_Toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed__main);

        newsFeedList = findViewById(R.id.newsFeedList);
        newsFeedET = findViewById(R.id.newsFeedET);
        newsFeedPB = findViewById(R.id.newsFeedPB);
        newsFeedSearch = findViewById(R.id.newsFeedSearch);
        sp = getSharedPreferences("fileName", Context.MODE_PRIVATE);
        isTablet = findViewById(R.id.feedFrameLayout) != null; //check if the FrameLayout is loaded
        loadSearchTerm();

        my_Toolbar = findViewById(R.id.newsFeed_toolbar);
        setSupportActionBar(my_Toolbar);

        newsFeedPB.setVisibility(View.VISIBLE);

        newsList = new ArrayList<>();
        db = new NewsFeed_Database(this);

        DataFetcher networkThread = new DataFetcher();
        Log.e("status", "created datafetcher thread");
        //starts background thread
        networkThread.execute();
        Log.e("status", "executed thread");

        newsFeedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSearchTerm();
                String search = newsFeedET.getText().toString();
                int counter = 0;

                for (int i = 0; i < newsList.size(); i++) {
                    if (newsList.get(i).getNewsTitle().contains(newsFeedET.getText().toString())) {
                        counter++;
                    }
                }
                if (counter > 0) {
                    Toast.makeText(NewsFeed_MainActivity.this, "Found "+counter+" results for " + newsFeedET.getText().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewsFeed_MainActivity.this, "There is no newsfeed about this", Toast.LENGTH_LONG).show();
                }
            }
        });


        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_MainActivity.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_choice, null);

                Button cancel = menuDetails.findViewById(R.id.newsCancelBtn);
                Button saveArticles = menuDetails.findViewById(R.id.saveNews);
                Button viewArticles = menuDetails.findViewById(R.id.openNews);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                viewArticles.setOnClickListener(c->{
                    String value = newsList.get(position).getURL();
                    Intent nextArticle = new Intent(NewsFeed_MainActivity.this,NewsFeed_Detail.class);
                    nextArticle.putExtra("URL", value);
                    menuDialog.hide();
                    startActivity(nextArticle);
                });

                saveArticles.setOnClickListener(c->{
                    db.insertData(newsList.get(position).getNewsTitle(),newsList.get(position).getNewsBody(),newsList.get(position).getURL(), newsList.get(position).getImageURL());
                    Toast.makeText(NewsFeed_MainActivity.this, "Article saved.", Toast.LENGTH_LONG).show();
                    menuDialog.hide();
                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
            }
        });
    }

    protected void saveSearchTerm() {
        //EditText term = findViewById(R.id.nyTypeSearch);
        String whatWasTyped = newsFeedET.getText().toString();
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
        newsFeedET.setText(saved);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsfeed_menu, menu);
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
            case R.id.newsFeedHelp:
                Toast.makeText(NewsFeed_MainActivity.this, "NewsFeed App, created by Felipe Magnago", Toast.LENGTH_LONG).show();
                break;
            case R.id.newsFeedMenu:
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_MainActivity.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_popup, null);

                Button cancel = menuDetails.findViewById(R.id.newsfeedPopUpCancelButton);

                Button viewSavedArticles = menuDetails.findViewById(R.id.goToSaved);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                viewSavedArticles.setOnClickListener(c->{
                    if(isTablet) {
                        NewsFeed_Saved_Fragment savedFragment = new NewsFeed_Saved_Fragment(); //add a DetailFragment
                        savedFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.feedFrameLayout, savedFragment) //Add the fragment in FrameLayout
                                .addToBackStack("Back") //make the back button undo the transaction
                                .commit(); //actually load the fragment.
                        menuDialog.hide();
                    }
                    else { //isPhone
                        Intent intent = new Intent(NewsFeed_MainActivity.this, NewsFeed_Saved.class);
                        menuDialog.hide();
                        startActivity(intent);
                    }
                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
        }
        return true;
    }

    private class DataFetcher extends AsyncTask<String, Integer, String> {
        List<NewsFeed> news = new ArrayList<>();
        String title = null;
        String urlText = null;
        String articleText = null;
        private Bitmap myImage = null;
        @Override
        protected String doInBackground(String... params) {
            Log.e("doinBackGround","executed.");

            try {

                //JSON
                // URL
                URL jurl = new URL("https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=e935b3481a374b0ab4bc39b5e6bea024");
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
                JSONArray results = jObject.getJSONArray("articles");
                for (int index = 0; index < results.length(); index++) {

                        news.add(new NewsFeed(index, results.getJSONObject(index).getString("title"),
                                results.getJSONObject(index).getString("description"), results.getJSONObject(index).getString("url"),
                                results.getJSONObject(index).getString("urlToImage")));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("status:", "finished background");
            return "finished task";

        }

        /**
         * Method that adds all the newsfeed found in the json from web server to the local arraylist of articles
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            Log.e("onPostExecute", "executed!");
            for (NewsFeed a : news) {
                newsList.add(a);
                Log.e("status", "added item to arraylist news");

            }
            NewsFeed_Adapter adapter = new NewsFeed_Adapter(newsList, getApplicationContext());
            newsFeedList.setAdapter(adapter);
            newsFeedPB.setVisibility(View.INVISIBLE);
            Log.e("status", "Adapter set for listview");
            Log.e("status:", "done with postExecute");
        }
    }
}


