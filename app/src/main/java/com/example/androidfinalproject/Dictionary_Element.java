package com.example.androidfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
/**
 * Display selected word from Dictionary_MainActivity if devise is phone
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class Dictionary_Element extends AppCompatActivity {
    private TextView wordTextView;
    private TextView definitionTextView;
    private Toolbar dictToolBar;
    private Dictionary_DataBase db;
    private Button delFromDataBaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_element);

        wordTextView = findViewById(R.id.wordTitle);
        definitionTextView = findViewById(R.id.wordDefinition);

        Intent current = getIntent();
        int wordID = current.getIntExtra("id", -1);
        String wordTitle = current.getStringExtra("title");
        String wordDefinition = current.getStringExtra("definition");

        wordTextView.setText(wordTitle);
        definitionTextView.setText(wordDefinition);

        Log.e("Dict_Element", wordTitle);

        dictToolBar = findViewById(R.id.DictToolBar);
        setSupportActionBar(dictToolBar);
        db = new Dictionary_DataBase(this);
        /**
         * deletes selected word from DictionaryDataBase
         **/
        delFromDataBaseButton = findViewById(R.id.DictDeleteButton);
        delFromDataBaseButton.setOnClickListener(e -> {
            Snackbar sb = Snackbar.make(delFromDataBaseButton, R.string.dictDelete, Snackbar.LENGTH_LONG);
            sb.setAction(R.string.dictDelSnackBar, c -> db.deleteData(wordID));
            sb.show();
            Log.e("db state", "word deleted!");
        });
    }
}
