package com.example.androidfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
/**
 * Adapter class for Dictionary_MainActivity listView
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class DictionaryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<DictionaryDefinition> wordList;
    /**
     * Constructs the adapter from a List of words and context
     * @param words
     * @param context
     */
    public DictionaryAdapter(List<DictionaryDefinition> words, Context context) {
        this.wordList = words;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return wordList.size();
    }
    @Override
    public Object getItem(int position) {
        return wordList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * Function to inflate views inside the words List
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView root = (TextView) convertView;
        if (convertView == null) {
            root = (TextView) inflater.inflate(R.layout.list_item, parent, false);
        }
        String toDisplay = wordList.get(position).getTitle();
        root.setText(toDisplay);
        return root;
    }
}
