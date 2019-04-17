package com.example.androidfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Activity with the list of saved articles. Retrieves list from articles saved on local database
 *
 * @author Rodrigo Eltz
 * @since 10-04-2019
 */
public class NYT_savedArticles extends AppCompatActivity {
    private Toolbar helpBar;
    private Button goBack;
    private ListView savedArticles;
    private List<Article> myArticles;
    private NYT_DataBase db;
    private ArticleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyt_saved_articles);
        helpBar = findViewById(R.id.nyToolbarHelp3);
        setSupportActionBar(helpBar);
        savedArticles = findViewById(R.id.listViewSaved);
        myArticles = new ArrayList<>();
        db = new NYT_DataBase(this);

        goBack = findViewById(R.id.nytBack);

        goBack.setOnClickListener(b -> {
            Intent goFeed = new Intent(NYT_savedArticles.this, NYTimes_MainActivity.class);
            startActivity(goFeed);
        });
        viewData();
        if (myArticles.size() == 0) {
            alertEmpty();
        }
        savedArticles.setClickable(true);
        savedArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent nextArticle = new Intent(NYT_savedArticles.this, FullArticleSaved.class);
                nextArticle.putExtra("title", myArticles.get(position).getTitle());
                nextArticle.putExtra("body", myArticles.get(position).getBody());
                nextArticle.putExtra("link", myArticles.get(position).getLink());
                nextArticle.putExtra("imageLink", myArticles.get(position).getImageLink());
                nextArticle.putExtra("id", myArticles.get(position).getNewsID());


                //TRYING SOMETHING
                // startActivity(nextArticle);
                startActivityForResult(nextArticle, 1);
            }
        });


    }

    /**
     * Method that creates dialog box to alert user that saved articles database is empty
     */
    public void alertEmpty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.emptyList).setPositiveButton(R.string.nyUnderstood, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });

        builder.create().show();
    }

    /**
     * Method that retrieves the articles from database and renders it into a listview
     */
    public void viewData() {
        Cursor cursor = db.viewData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Article article = new Article(cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(0));
                myArticles.add(article);
                Log.e("view status", "article added to list");
            }
            adapter = new ArticleAdapter(myArticles, this);

            savedArticles.setAdapter(adapter);
            Log.e("status", "adapter set!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("activityResultCalled", "yes");
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Log.e("resquestCorrect", "12Yes");
                int deleteID = data.getIntExtra("deletedID", -1);
                if (deleteID >= 0) {


                  Article deleteThis=null;
                    for (Article a: myArticles) {
                        if (a.getNewsID()==deleteID) {
                            deleteThis=a;
                        }
                    }
                    myArticles.remove(deleteThis);

                    adapter.notifyDataSetChanged();
                }
            }
        }
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


}
