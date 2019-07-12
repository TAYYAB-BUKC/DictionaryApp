package com.example.dictionaryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
