package com.example.androidfinalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Dictionary Fragment to load contents on same activity if isTablet
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class Dictionary_Fragment extends Fragment {
    private Dictionary_DataBase db;
    private TextView wordTextView;
    private TextView definitionTextView;
    private Button delFromDataBaseButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new Dictionary_DataBase(getActivity());

        //Inflate layout for fragment
        View result = inflater.inflate(R.layout.dict_element, container, false);

        wordTextView = result.findViewById(R.id.wordTitle);
        definitionTextView = result.findViewById(R.id.wordDefinition);

        Bundle current = getArguments();

        int wordID = current.getInt("id", -1);
        String wordTitle = current.getString("title");
        String wordDefinition = current.getString("definition");

        wordTextView.setText(wordTitle);
        definitionTextView.setText(wordDefinition);
        Log.e("Dict_Element", wordTitle);

        /**
         * Deletes the entry from the saved words list
         **/
        delFromDataBaseButton = result.findViewById(R.id.DictDeleteButton);
        delFromDataBaseButton.setOnClickListener(e -> {
            db.deleteData(wordID);
            Log.e("db state", "word deleted!");
            Toast.makeText(getActivity(), R.string.dictDelete,
                    Toast.LENGTH_LONG).show();
        });

        return result;
    }
}