package com.example.androidfinalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Dictionary Main Activity
 * Search for definitions online and hows list of saved words
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class Dictionary_MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Toolbar dictToolBar;
    private Button dictSearchBtn;
    private EditText dictSearchEditText;
    private List<DictionaryDefinition> wordList;
    private ListView savedWords;
    private Dictionary_DataBase db;
    public DictionaryAdapter adapter;
    private boolean isTablet;


    /**
     * Creates Dictionary_MainActivity
     * @param savedInstanceState
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_main);

        sp = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        dictToolBar = findViewById(R.id.DictToolBar);
        setSupportActionBar(dictToolBar);

        dictSearchBtn = findViewById(R.id.DictSearchButton);
        dictSearchEditText = findViewById(R.id.DictSearchEditText);

        loadSearchTerm();

        savedWords = findViewById(R.id.wordListView);
//        wordList = new ArrayList<>();

        db = new Dictionary_DataBase(this);

        isTablet = (findViewById(R.id.fragmentLocation) != null);

//        viewData();

        savedWords.setClickable(true);
        /**
         * Menu selection of saved words
         **/
        savedWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextArticle = new Intent(Dictionary_MainActivity.this, Dictionary_Element.class);
                nextArticle.putExtra("id", wordList.get(position).getId());
                nextArticle.putExtra("title", wordList.get(position).getTitle());
                nextArticle.putExtra("definition", wordList.get(position).getDefinition());
                if (isTablet){
                    Bundle dataToPass = new Bundle();
                    dataToPass.putInt("id", wordList.get(position).getId());
                    dataToPass.putString("title", wordList.get(position).getTitle());
                    dataToPass.putString("definition", wordList.get(position).getDefinition());
                    Log.e("dict_main_isTablet","yes!");
                    Dictionary_Fragment dfrag = new Dictionary_Fragment();
                    dfrag.setArguments(dataToPass);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragmentLocation, dfrag)
                            .addToBackStack("anyName")
                            .commit();
                } else {
                    startActivity(nextArticle);
                }
            }
        });
        /**
         * search button for definitions online
         **/
        dictSearchBtn.setOnClickListener(e -> {
            if (dictSearchEditText != null) {
                String wordToSearch = dictSearchEditText.getText().toString();

                Intent toSearch = new Intent(Dictionary_MainActivity.this, Dictionary_SearchActivity.class);
                toSearch.putExtra("term", wordToSearch);
                startActivity(toSearch);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        adapter.notifyDataSetChanged();
        wordList = new ArrayList<>();
        viewData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.saveSearchTerm();
    }
    /**
     * Saves the current search word
     **/
    protected void saveSearchTerm() {
        String whatWasTyped = dictSearchEditText.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("savedTerm", whatWasTyped);
        editor.commit();
    }
    /**
     * Loads the last search word to the search editText.
     */
    protected void loadSearchTerm() {
        String saved = sp.getString("savedTerm", "");
        dictSearchEditText.setText(saved);
    }
    /**
     * Displays all current elements in the Dictionary DataBase
     **/
    public void viewData() {
        Cursor cursor = db.viewData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                DictionaryDefinition word = new DictionaryDefinition(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                wordList.add(word);
                Log.e("Main_view status", word.getTitle());
            }
            adapter = new DictionaryAdapter(wordList, this);
            savedWords.setAdapter(adapter);
            Log.e("Main_status", "adapter set!");
        }
    }
    /**
     * Inflates the toolBar menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dict_menu, menu);

        return true;
    }
    /**
     * Make toolBar selection
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.homeIcon:
                goHomeAlert();
                break;
            case R.id.helpIcon:
                Toast.makeText(this, getString(R.string.dictAbout), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    /**
     * Go Home menu alert, finish the Dictionary activity
     **/
    public void goHomeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to return to apps selection?")
                .setPositiveButton(getString(R.string.dictPositive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.dictNegative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                });
        builder.create().show();
    }
}