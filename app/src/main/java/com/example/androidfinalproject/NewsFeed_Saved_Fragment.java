package com.example.androidfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed_Saved_Fragment extends Fragment {
    private Bundle dataFromActivity;
    private NewsFeed_Database db;
    private ListView newsFeedSavedList;
    private List<NewsFeed> myArticles;
    private NewsFeed_Adapter adapter;

    /**
     * check if it is a tablet
     */
    private boolean isTablet;

    /**
     * set tablet
     * @param tablet
     *
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        Log.e("status", "Entered fragment");
        if (container != null) {
            container.removeAllViews();
        }
        myArticles = new ArrayList<>();
        db = new NewsFeed_Database(getActivity());
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_news_feed__saved, container, false);
        newsFeedSavedList = view.findViewById(R.id.newsFeedSavedList);

        Cursor cursor = db.viewData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                NewsFeed article = new NewsFeed(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4));
                myArticles.add(article);
                Log.e("view status", "article added to list");
            }
            adapter = new NewsFeed_Adapter(myArticles, getActivity());

            newsFeedSavedList.setAdapter(adapter);
            Log.e("status", "adapter set!");
        }

            newsFeedSavedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder menuBuilder = new AlertDialog.Builder(getActivity());
                    @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.news_feed_choice2, null);
                    Button cancel = menuDetails.findViewById(R.id.newsCancelBtn2);
                    Button deleteArticles = menuDetails.findViewById(R.id.deleteNews);
                    Button viewArticles = menuDetails.findViewById(R.id.openNews2);

                    menuBuilder.setView(menuDetails);
                    AlertDialog menuDialog = menuBuilder.create();
                    menuDialog.show();

                    viewArticles.setOnClickListener(c->{
                        String value = myArticles.get(position).getURL();
                        Intent nextArticle = new Intent(getActivity(),NewsFeed_Detail.class);
                        nextArticle.putExtra("URL", value);
                        menuDialog.hide();
                        startActivity(nextArticle);
                    });

                    deleteArticles.setOnClickListener(c->{
                        db.deleteData(myArticles.get(position).getNewsId());
                        myArticles.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Article deleted.", Toast.LENGTH_LONG).show();
                        menuDialog.hide();
                    });

                    cancel.setOnClickListener(c->{
                        menuDialog.hide();
                    });

                }
            });
        return view;

    }
}
