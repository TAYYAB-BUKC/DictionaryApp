package com.example.dictionaryapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

public class LoadDatabaseAsync extends AsyncTask {

    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelper databaseHelper;


    public LoadDatabaseAsync(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();


        AlertDialog.Builder d = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_diolog_database_copying, null);
        d.setTitle("Loading Database...");
        d.setView(dialogView);
        alertDialog = d.create();

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        databaseHelper = new DatabaseHelper(context);

        try {
            databaseHelper.createDataBase();

        } catch (IOException e) {

            throw new Error("Database was not created");
        }
        databaseHelper.close();
        return null;
    }


    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        alertDialog.dismiss();
        MainActivity.openDatabase();
    }
}