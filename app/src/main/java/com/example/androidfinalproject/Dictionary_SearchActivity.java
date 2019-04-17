package com.example.androidfinalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Search activity for dictionary
 * Display the definition of searched word and add word to saved words
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class Dictionary_SearchActivity extends AppCompatActivity {
    private TextView wordTextView;
    private TextView definitionTextView;
    private ProgressBar progress;
    private Toolbar dictToolBar;
    private String wordToSearch;
    private Dictionary_DataBase db;
    private Button saveToDataBaseButton;
    /**
     * Creates Dictionary_SearchActivity
     * @param savedInstanceState
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_search_activity);

        wordTextView = findViewById(R.id.wordTitle);

        Intent current = getIntent();
        wordToSearch = current.getStringExtra("term");
        wordTextView.setText(wordToSearch);
        Log.e("Dict_SearchActivity", wordToSearch);


        definitionTextView = findViewById(R.id.wordDefinition);
        progress = findViewById(R.id.searchProgress);
        dictToolBar = findViewById(R.id.DictToolBar);
        setSupportActionBar(dictToolBar);
        db = new Dictionary_DataBase(this);

        saveToDataBaseButton = findViewById(R.id.DictSaveButton);
        saveToDataBaseButton.setOnClickListener(e -> {
            db.insertData(wordToSearch, definitionTextView.getText().toString());
            Log.e("db state","word saved!");
            Toast.makeText(getApplicationContext(), R.string.dictSaved,
                    Toast.LENGTH_LONG).show();
        });

        DictionaryQuery networkThread = new DictionaryQuery();
        Log.e("status","created data fetcher thread");
        networkThread.execute();
        Log.e("status","executed thread");
    }
    /**
     * This class is responsible for Dictionary JSON Queries for online search
     **/
    private class DictionaryQuery extends AsyncTask<String, Integer, String> {
        /**
         * Search results on Meridian Webster Dictionary
         **/
        @Override
        protected String doInBackground(String... params) {
            String parseResult = "";
            try {
                //JSON
                // URL
                progress.setVisibility(View.VISIBLE);
                publishProgress(0);


                wordToSearch = URLEncoder.encode( wordToSearch, "UTF-8");
                String urlWithTerm = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + wordToSearch + "?key=4556541c-b8ed-4674-9620-b6cba447184f";
                Log.e("JsonLink",urlWithTerm);
                URL jurl = new URL(urlWithTerm);
                HttpURLConnection jurlConnection = (HttpURLConnection) jurl.openConnection();

                jurlConnection.setReadTimeout(10000 /* milliseconds */);
                jurlConnection.setConnectTimeout(15000 /* milliseconds */);
                jurlConnection.setRequestMethod("GET");
                jurlConnection.setDoInput(true);
                //Starts the query
                jurlConnection.connect();
                Log.e("DictionaryQuery", "connection establishes");

                //Create XML parser
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(jurlConnection.getInputStream(), null);
                parser.nextTag();
                Log.e("DictionaryQuery", "pull parser created");
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();
                        if (tagName.equals("dt")) {
                            parser.next();
                            parseResult = parser.getText();
//                            definitionTextView.setText(parser.getText());
                            publishProgress(50);
                        }
                    }
                    parser.next();
                }
                publishProgress(100);
                progress.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("status:", "finished background");
            return parseResult;
        }
        /**
         * sets the definition TextView with the result of the query
         * @param parseResult
         **/
        @Override
        protected void onPostExecute(String parseResult) {
            definitionTextView.setText(parseResult);
        }
    }
}