package com.example.androidfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NYT_search_result extends AppCompatActivity {
    private Button goBack;
    private ListView nyFeed;
    private ProgressBar progress;
    private List<Article> newsList;
    private Toolbar helpBar;
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nyt_search_result);
        // gets the search term from previous activity

        Intent current = getIntent();
        term = current.getStringExtra("term");
        Log.e("searchTerm",term);

        goBack = findViewById(R.id.nyBackButton);
        newsList = new ArrayList<>();
        progress = findViewById(R.id.searchProgress);
        helpBar = findViewById(R.id.nyToolbarHelp);
        nyFeed = findViewById(R.id.listView2);
        setSupportActionBar(helpBar);

        //temporarelly invisible
        progress.setVisibility(View.VISIBLE);

        goBack.setOnClickListener(b -> {
            finish();
        });

        //FETCH DATA!
        DataFetcher networkThread = new DataFetcher();
        Log.e("status","created datafetcher thread");
        networkThread.execute();
        Log.e("status","executed thread");

        nyFeed.setClickable(true);
        nyFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent nextArticle = new Intent(NYT_search_result.this, FullArticle.class);
                nextArticle.putExtra("title", newsList.get(position).getTitle());
                nextArticle.putExtra("body", newsList.get(position).getBody());
                nextArticle.putExtra("link", newsList.get(position).getLink());
                nextArticle.putExtra("imageLink", newsList.get(position).getImageLink());

                startActivity(nextArticle);

            }
        });
    }

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
                Intent goSavedNow = new Intent(NYT_search_result.this, NYT_savedArticles.class);
                startActivity(goSavedNow);
                break;
        }
        return true;
    }

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

    private class DataFetcher extends AsyncTask<String, Integer, String> {
        private List<Article> news = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {

            try {

                //JSON
                // URL
                String urlWithTerm = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + term + "&api-key=6Y1zFdkuCRAVyJAQBwhgB9x3Dgw1F5JA";
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
                JSONObject searchResults = jObject.getJSONObject("response");
                Log.e("status","got Jsonobject response");
                JSONArray results = searchResults.getJSONArray("docs");
                Log.e("status","Got jsonArray docs");
                for (int index = 0; index < results.length(); index++) {
                        JSONObject headline = results.getJSONObject(index).getJSONObject("headline");
                        String mainTitle = headline.getString("main");
                        String theLink = results.getJSONObject(index).getString("web_url");
                        String mainText = results.getJSONObject(index).getString("lead_paragraph");
                        JSONArray images = results.getJSONObject(index).getJSONArray("multimedia");
                        boolean noImages = images.isNull(0);
                        String imageLink="https://static01.nyt.com/";
                        if (!noImages) {
                            imageLink += images.getJSONObject(1).getString("url");
                            Log.e("hasImage","yes");
                        }
                        news.add(new Article(mainTitle,mainText,theLink,imageLink,index));
                                Log.e("status articles","loaded article");
                        Log.e("image link is",imageLink);

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
