package com.example.androidfinalproject;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Class that defines the fragment version, for when search activity runs on a tablet
 * @author Rodrigo Eltz
 * @since 10-04-2019
 * @version  2.0
 **/
public class NYTfragment extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private NYT_DataBase db;


    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        Log.e("status", "Entered fragment");
        if (container != null) {
            container.removeAllViews();
        }
        db = new NYT_DataBase(getActivity());

        //Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_full_article, container, false);

        TextView title = result.findViewById(R.id.news_title_detailed);
        TextView body = result.findViewById(R.id.news_body);
        TextView link = result.findViewById(R.id.nyArticleLink);


        //ImageView image = result.findViewById(R.id.nyImageArticle);
        Toolbar helpBar = result.findViewById(R.id.nyToolbarHelp2);
        Button saveArticle = result.findViewById(R.id.nyButtonSave);

        dataFromActivity = getArguments();


        String stringTitle = (dataFromActivity.getString("title"));
        String stringBody = dataFromActivity.getString("body");
        String stringLink = dataFromActivity.getString("link");
        title.setText(stringTitle);
        body.setText(stringBody);
        link.setText(stringLink);

        saveArticle.setOnClickListener(b -> {
            db.insertData(stringTitle, stringBody, stringLink, "");
            Toast.makeText(getActivity(), "Article saved!",
                    Toast.LENGTH_LONG).show();
        });


        return result;


    }
}
