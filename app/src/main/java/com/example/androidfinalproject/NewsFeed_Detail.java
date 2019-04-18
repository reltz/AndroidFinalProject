package com.example.androidfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
 * NewsFeed_Detail Class for the News Feed
 * @author Felipe Magnago
 * @since 17-04-2019
 * @version  1.0
 **/
public class NewsFeed_Detail extends AppCompatActivity {

    android.support.v7.widget.Toolbar my_Toolbar;
    ProgressBar newsFeedPB2;
    String URL;
    WebView webView;
    WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news_feed__detail);
        setContentView(R.layout.activity_news_feed_webview);

        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");

        my_Toolbar = findViewById(R.id.newsFeedDetail_toolbar);
        setSupportActionBar(my_Toolbar);

        newsFeedPB2 = findViewById(R.id.newsFeedPB2);
        newsFeedPB2.setMax(100);

        //webview
        webView = findViewById(R.id.newsFeedWebView);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //improve webview performance
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL);




        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                newsFeedPB2.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                newsFeedPB2.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

        //newsFeedPB2.setVisibility(View.INVISIBLE);
        Log.e("Load Page", "executed");
    }
    /**
     * Method that inflates the Menu for the toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsfeed_menu2, menu);
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
                break;
        }
        return true;
    }
}

