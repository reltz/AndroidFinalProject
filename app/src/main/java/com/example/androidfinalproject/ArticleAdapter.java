package com.example.androidfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Article> article;


    public ArticleAdapter(List<Article> news, Context context) {
        this.article = news;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return article.size();
    }

    @Override
    public Object getItem(int position) {
        return article.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        TextView root = (TextView) convertView;
        if (convertView== null) {
            root = (TextView) inflater.inflate(R.layout.list_item,parent,false);
                    }

        String toDisplay = article.get(position).getTitle();

        root.setText(toDisplay);
        return root;
    }




}
