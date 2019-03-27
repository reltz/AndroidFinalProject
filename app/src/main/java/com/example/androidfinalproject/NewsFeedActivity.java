package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewsFeedActivity extends AppCompatActivity {

    ArrayList<SearchActivity> newsFeedArray;
    EditText newsFeedET;
    Button newsFeedSearch;
    ProgressBar newsFeedPB;
    ListView newsFeedList;
    SearchActivity search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        newsFeedList = findViewById(R.id.newsFeedList);
        newsFeedET = findViewById(R.id.newsFeedET);
        newsFeedPB = findViewById(R.id.newsFeedPB);
        newsFeedSearch = findViewById(R.id.newsFeedSearch);
        newsFeedArray = new ArrayList<>();

        final ListAdapter aListAdapter = new ListAdapter(newsFeedArray, getApplicationContext());
        newsFeedList.setAdapter(aListAdapter);

        newsFeedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(NewsFeedActivity.this, "Results for " + newsFeedET.getText().toString(), Toast.LENGTH_LONG).show();
                    newsFeedArray.clear();
                    newsFeedPB.setVisibility(View.VISIBLE);
                    newsFeedPB.setMax(19);
                    for (int i = 0; i < 20; i++) {
                        search = new SearchActivity(i,newsFeedET.getText().toString(), true);
                        newsFeedArray.add(search);
                        newsFeedPB.setProgress(i);
                    }

                    //newsFeedPB.setVisibility(View.INVISIBLE);
                    //Thread.sleep(1500);
                    aListAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("Getting Article failed", ex.getMessage() );
                }
            }
        });

        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Item click" , "Item "+ position + " clicked.");
                Snackbar.make(newsFeedList, "Good Choice!", Snackbar.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeedActivity.this);
                @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.dialog_detail, null);


                TextView newsItem = details.findViewById(R.id.dialogNewsItem);
                newsItem.setText("You clicked on " + newsFeedArray.get(position).getSearch() + " " + newsFeedArray.get(position).getID());

                builder.setView(details);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

//    private class newsFeed extends AsyncTask<String, Integer, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return null;
//        }
//    }

    }


    protected class ListAdapter extends BaseAdapter {

        private List<SearchActivity> msg;
        private Context ctx;
        private LayoutInflater inflater;

        public ListAdapter(List<SearchActivity> msg, Context ctx) {
            this.msg = msg;
            this.ctx = ctx;
            this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return msg.size();
        }

        @Override
        public Object getItem(int position) {
            return msg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (msg.get(position).isClicked()) {
                convertView = inflater.inflate(R.layout.activity_search, null);
            }
            TextView messageText = convertView.findViewById(R.id.newsFeedItem);
            messageText.setText(msg.get(position).getSearch());
            return convertView;
        }
    }

}
