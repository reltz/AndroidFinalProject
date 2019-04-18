package com.example.androidfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class Dictionary_DataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "DictionaryDB";
    /**
     * Generates table
     */
    private static final String DB_TABLE = "savedWords";
    //Columns
    private static final String COL_ID = "WordID";
    private static final String COL_TITLE = "Title";
    private static final String COL_DEFINITION = "Definition";
    //query to create table
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITLE + " TEXT, " + COL_DEFINITION + " TEXT);";
    //default constructor
    public Dictionary_DataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Dictionary_DataBase", "before creation db");
        db.execSQL(CREATE_TABLE);
        Log.e("Dictionary_DataBase", "AFTER creation db");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
    /**
     * Method that handdles insertion to the database and returns 1 if inserted, else -1.
     * @param title
     * @param definition
     * @return
     */
    public boolean insertData(String title, String definition) {
        long result = -1;
        Log.e("Dictionary_DataBase", "before INSERT db");
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COL_TITLE, title);
            cv.put(COL_DEFINITION, definition);
            result = db.insert(DB_TABLE, null, cv);
            Log.e("Dictionary_DataBase", "AFTER INSERT db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Dictionary_DataBase", "AFTER INSERT METHOD db");
        return result != -1;
    }
    /**
     * Method that handles deletion of data from the database and returns 1 if deleted, else -1.
     * @param ID
     * @return
     */
    public boolean deleteData(int ID) {
        long result = -1;
        try {
            String id = Integer.toString(ID);
            SQLiteDatabase db = this.getWritableDatabase();
            result=db.delete(DB_TABLE, COL_ID + "=?", new String[]{id});
            Log.e("DICT_DB", "deleted entry!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result!=-1;
    }
    /**
     * Cursor to retrieve data from database and render it
     * @return
     */
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        return cursor;
    }
}