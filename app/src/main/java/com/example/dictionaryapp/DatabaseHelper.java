package com.example.dictionaryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAME = "Eng_Dictionary.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAME);
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[32];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

       // Toast.makeText(myContext, "Database Copied successfully", Toast.LENGTH_SHORT).show();
        // Log.i("copyDataBase", "Database copied");
    }


    public boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }


    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {

            this.getReadableDatabase();
            try {
                myContext.deleteDatabase(DB_NAME);
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public Cursor getMeaning(String text)
    {
        Cursor c= myDataBase.rawQuery("SELECT en_definition,example,synonyms,antonyms FROM words WHERE en_word==UPPER('"+text+"')",null);
        return c;
    }

    public Cursor getSuggestions(String text)
    {
        Cursor c= myDataBase.rawQuery("SELECT _id, en_word FROM words WHERE en_word LIKE '"+text+"%' LIMIT 40",null);
        return c;
    }

    public void  insertHistory(String text)
    {
        myDataBase.execSQL("INSERT INTO history(word) VALUES(UPPER('"+text+"'))");
    }

    public Cursor getHistory()
    {
        Cursor c= myDataBase.rawQuery("select distinct word, en_definition from history h join words w on h.word==w.en_word order by h._id desc",null);
        return c;
    }

    public void  deleteHistory()
    {
        myDataBase.execSQL("DELETE FROM history");
    }




}
