package com.example.dictionaryapp;

import android.database.Cursor;
import android.database.SQLException;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dictionaryapp.Fragments.Fragment_Antonyms;
import com.example.dictionaryapp.Fragments.Fragment_Definition;
import com.example.dictionaryapp.Fragments.Fragment_Example;
import com.example.dictionaryapp.Fragments.Fragment_Synonyms;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaningActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton buttonSpeak;
    String searchWord;
    DatabaseHelper databaseHelper;
    Cursor cursor = null;
    public String enDefinition;
    public String example;
    public String synonyms;
    public String antonyms;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        //received values
        Bundle bundle = getIntent().getExtras();
        searchWord= bundle.getString("en_word");

        databaseHelper = new DatabaseHelper(this);

        try {
            databaseHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        cursor = databaseHelper.getMeaning(searchWord);

        if (cursor.moveToFirst()) {

            enDefinition= cursor.getString(cursor.getColumnIndex("en_definition"));
            example=cursor.getString(cursor.getColumnIndex("example"));
            synonyms=cursor.getString(cursor.getColumnIndex("synonyms"));
            antonyms=cursor.getString(cursor.getColumnIndex("antonyms"));
        }

        databaseHelper.insertHistory(searchWord);

        buttonSpeak = (ImageButton) findViewById(R.id.buttonSpeak);

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    textToSpeech = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status == TextToSpeech.SUCCESS){
                                int result=textToSpeech.setLanguage(Locale.getDefault());
                                if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                                    Log.e("error", "This Language is not supported");
                                }
                                else{
                                    textToSpeech.speak(searchWord, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                            else
                                Log.e("error", "Initialization Failed!");
                        }
                    });
            }
        });





        toolbar = (Toolbar) findViewById(R.id.wordMeaningtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(searchWord);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        viewPager = findViewById(R.id.tabViewPager);

        if(viewPager != null){
            setupViewPager(viewPager);
        }
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

        public void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new Fragment_Definition(), "Definition");
            adapter.addFrag(new Fragment_Synonyms(), "Synonyms");
            adapter.addFrag(new Fragment_Antonyms(), "Antonyms");
            adapter.addFrag(new Fragment_Example(), "Example");
            viewPager.setAdapter(adapter);
        }
}
