package com.example.androidfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database auxiliary class to handle the saved articles database: NYT_Saved_Articles
 * @author Rodrigo Eltz
 * @since 10-04-2019
 */
public class NYT_DataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "NYT_Saved_Articles";

    /**
     * Generates table
     */
    private static final String DB_TABLE = "savedArticles";

    //Columns
    private static final String COL_ID = "ArticleID";
    private static final String COL_TITLE = "ArticleTitle";
    private static final String COL_ABSTRACT = "ArticleAbstract";
    private static final String COL_LINK = "ArticleLink";
    private static final String COL_IMAGE = "ArticleImage";

    //query to create table
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITLE + " TEXT, " + COL_ABSTRACT + " TEXT, " + COL_LINK + " TEXT," + COL_IMAGE + " TEXT);";

    //default constructor
    public NYT_DataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    /**
     * Method that handdles insertion to the database and returns 1 if inserted, else -1.
     * @param title
     * @param articleAbstract
     * @param link
     * @param imageLink
     * @return
     */
    public boolean insertData(String title, String articleAbstract, String link, String imageLink) {
        long result = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COL_TITLE, title);
            cv.put(COL_ABSTRACT, articleAbstract);
            cv.put(COL_LINK, link);
            cv.put(COL_IMAGE, imageLink);

            result = db.insert(DB_TABLE, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Log.e("status", "deleted entry!");
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

        return cursor;
    }
}
