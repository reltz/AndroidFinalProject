package com.example.androidfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsFeed_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<NewsFeed> news;

    public NewsFeed_Adapter(List<NewsFeed> news, Context context) {
        this.news = news;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public NewsFeed getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (news.get(position) != null) {
            convertView = inflater.inflate(R.layout.activity_news_feed_search, null);
        }

        TextView messageText = convertView.findViewById(R.id.newsFeedItem);
        messageText.setText(news.get(position).getNewsTitle());
        return convertView;

//        TextView root = (TextView) convertView;
//        if (convertView== null) {
//            root = (TextView) inflater.inflate(R.layout.activity_news_feed__detail,parent,false);
//        }
//
//        String toDisplay = news.get(position).getNewsTitle();
//
//        root.setText(toDisplay);
//        return root;
//        if (news.get(position).isClicked()) {
//            convertView = inflater.inflate(R.layout.activity_news_feed_search, null);
//        }
//        TextView messageText = convertView.findViewById(R.id.newsFeedItem);
//        messageText.setText(news.get(position).getNewsTitle());
//        return convertView;
    }
}
