package com.example.dictionaryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    static DatabaseHelper databaseHelper;
    static boolean databaseOpened=false;
    SimpleCursorAdapter suggestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
               /* Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                startActivity(intent);*/
            }
        });


        databaseHelper = new DatabaseHelper(this);

        if (databaseHelper.checkDataBase()) {
            openDatabase();

        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }

        // setup SimpleCursorAdapter

        final String[] from = new String[]{"en_word"};
        final int[] to = new int[]{R.id.suggestionTextview};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.suggestion_row, null, from, to, 0) {
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }

        };


        searchView.setSuggestionsAdapter(suggestionAdapter);


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                // Add clicked text to search box
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                searchView.setQuery(clicked_word, false);

                //search.setQuery("",false);

                searchView.clearFocus();
                searchView.setFocusable(false);

                Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word", clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchText = searchView.getQuery().toString();

                Cursor c = databaseHelper.getMeaning(searchText);

                if (c.getCount() == 0) {
                    showAlertDialog();
                }
                else{
                    //search.setQuery("",false);
                    searchView.clearFocus();
                    searchView.setFocusable(false);

                    Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",searchText);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                searchView.setIconifiedByDefault(false); //Give Suggestion list margins
                Cursor cursorSuggestion=databaseHelper.getSuggestions(s);
                suggestionAdapter.changeCursor(cursorSuggestion);

                return false;
            }
        });
    }


    private void showAlertDialog()
    {
        searchView.setQuery("",false);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Word Not Found");
        builder.setMessage("Try another word");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchView.clearFocus();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }



    protected static void openDatabase()
    {
        try {
            databaseHelper.openDataBase();
            databaseOpened=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_clearHistory) {
            Toast.makeText(this,"Clear History Item is selected from the Menu",Toast.LENGTH_LONG).show();
            return true;
        }
        else if(id == R.id.action_about){
            Toast.makeText(this,"About Item is selected from the Menu",Toast.LENGTH_LONG).show();
            System.exit(0);
            return true;
        }
        else if(id == R.id.action_exit){
            Toast.makeText(this,"Exit Item is selected from the Menu",Toast.LENGTH_LONG).show();
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
