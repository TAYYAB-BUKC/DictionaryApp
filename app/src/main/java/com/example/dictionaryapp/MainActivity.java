package com.example.dictionaryapp;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView)findViewById(R.id.search_view);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    searchView.setIconified(false);
                    Intent intent = new Intent(MainActivity.this,WordMeaningActivity.class);
                    startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_setting) {
            Toast.makeText(this,"Setting Item is selected from the Menu",Toast.LENGTH_LONG).show();
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
