package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed_Saved extends AppCompatActivity {
    android.support.v7.widget.Toolbar my_Toolbar;
    private ListView newsFeedSavedList;
    private NewsFeed_Database db;
    private List<NewsFeed> myArticles;
    private NewsFeed_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed__saved);

        newsFeedSavedList = findViewById(R.id.newsFeedSavedList);
        myArticles = new ArrayList<>();
        db = new NewsFeed_Database(this);

        my_Toolbar = findViewById(R.id.newsFeedDetail_toolbar);
        setSupportActionBar(my_Toolbar);

        viewData();

        newsFeedSavedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_Saved.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_choice2, null);

                Button cancel = menuDetails.findViewById(R.id.newsCancelBtn2);
                Button deleteArticles = menuDetails.findViewById(R.id.deleteNews);
                Button viewArticles = menuDetails.findViewById(R.id.openNews2);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                viewArticles.setOnClickListener(c->{
                    String value = myArticles.get(position).getURL();
                    Intent nextArticle = new Intent(NewsFeed_Saved.this,NewsFeed_Detail.class);
                    nextArticle.putExtra("URL", value);
                    menuDialog.hide();
                    startActivity(nextArticle);
                });

                deleteArticles.setOnClickListener(c->{
                    db.deleteData(myArticles.get(position).getNewsId());
                    myArticles.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NewsFeed_Saved.this, "Article deleted.", Toast.LENGTH_LONG).show();
                    menuDialog.hide();
                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
            }
        });
    }


    public void viewData() {
        Log.e("ViewData","Executed!");
        Cursor cursor = db.viewData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                NewsFeed article = new NewsFeed(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4));
                myArticles.add(article);
                Log.e("view status", "article added to list");
            }
            adapter = new NewsFeed_Adapter(myArticles, this);

            newsFeedSavedList.setAdapter(adapter);
            Log.e("status", "adapter set!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsfeed_menu2, menu);
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
                Toast.makeText(NewsFeed_Saved.this, "NewsFeed App, created by Felipe Magnago", Toast.LENGTH_LONG).show();
                break;
            case R.id.newsFeedMenu:
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsFeed_Saved.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_popup, null);

                Button cancel = menuDetails.findViewById(R.id.newsfeedPopUpCancelButton);

                Button viewSavedArticles = menuDetails.findViewById(R.id.goToSaved);

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

//                viewSavedArticles.setOnClickListener(c->{
//                    Intent intent = new Intent(NewsFeed_Saved.this, NewsFeed_Saved.class);
//                    startActivity(intent);
//                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });
        }
        return true;
    }
}
