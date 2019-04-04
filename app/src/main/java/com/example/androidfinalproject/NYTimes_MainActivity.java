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
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;

import org.json.JSONArray;
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

public class NYTimes_MainActivity extends AppCompatActivity {
    private Button goBack;
    private Button search;
    //private EditText typeSearch;
    private ListView nyFeed;
    private ProgressBar progress;
    private Toolbar helpBar;
    List<Article> newsList;

//NY times milestone 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_main);
        goBack = findViewById(R.id.nyBackButton);
        //typeSearch = findViewById(R.id.nyTypeSearch);
        search = findViewById(R.id.nySearchButton);
        nyFeed = findViewById(R.id.listView);
        progress = findViewById(R.id.indeterminateBar);
        helpBar = findViewById(R.id.nyToolbarHelp);
        setSupportActionBar(helpBar);

        //INVISIBLE PROGrESS
        progress.setVisibility(View.INVISIBLE);

        goBack.setOnClickListener(a -> {
            Snackbar sb = Snackbar.make(goBack, "Go Back?", Snackbar.LENGTH_LONG);
            sb.setAction("Confirm!", b -> finish());
            sb.show();
        });


        search.setOnClickListener(b -> {
            Intent searchList = new Intent(NYTimes_MainActivity.this, ArticleSearchList.class);
            startActivity(searchList);
        });


        newsList = new ArrayList<Article>();
        Log.e("status", "Created news array list");

        ArticleAdapter adapter = new ArticleAdapter(newsList, getApplicationContext());


        //newsList.add(new Article("test1", "empty", 0));

        DataFetcher networkThread = new DataFetcher();
        Log.e("status", "created datafetcher thread");
        //starts background thread
        networkThread.execute("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
        Log.e("status", "executed thread");


        nyFeed.setClickable(true);


        nyFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent nextArticle = new Intent(NYTimes_MainActivity.this, FullArticle.class);
                    startActivity(nextArticle);
                }


            }
        });
        nyFeed.setAdapter(adapter);
        Log.e("status", "Adapter set for listview");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nytimes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nytHelp:
                alertNytHelp();
                break;
        }
        return true;
    }

    public void alertNytHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Author: Rodrigo Eltz" + "\n" + "Version: 1.0" +
                "\n\n" +
                "Instructions: This app shows a list of latest New York Times Article." +
                " You can select one to read and save to your favorites, as well as search " +
                "for articles using the search box.").setPositiveButton("Understood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });

        builder.create().show();
    }


    private class DataFetcher extends AsyncTask<String, Integer, String> {
        private List<Article> news = new ArrayList<>();
        private int index = 0;

        @Override
        protected String doInBackground(String... params) {

            try {
                //get the string url:
                String myUrl = params[0];


                //create the network connection
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                Log.e("response", inStream.toString());


                //JSON
                // URL
                URL jurl = new URL("https://api.nytimes.com/svc/news/v3/content/all/all.json?api-key=6Y1zFdkuCRAVyJAQBwhgB9x3Dgw1F5JA");
                HttpURLConnection jurlConnection = (HttpURLConnection) jurl.openConnection();
                inStream = jurlConnection.getInputStream();

                //create JSON object for response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line=reader.readLine())!=null) {
                    sb.append(line+"\n");
                }
                String result = sb.toString();


                //now a json table
                JSONObject jObject = new JSONObject(result);
                JSONArray results = jObject.getJSONArray("results");
                        //.getJSONObject(0).getString("title");
                for (int index=0; index<results.length(); index++) {
                    news.add(new Article(results.getJSONObject(index).getString("title"),
                            results.getJSONObject(index).getString("abstract"),index));
                }







            } catch (Exception e) {
                e.printStackTrace();
            }
            return "finished task";
        }

        @Override
        protected void onPostExecute(String s) {
            for (Article a : news) {
                newsList.add(a);
                Log.e("status", "added item to arraylist news");

            }
        }
    }


}
