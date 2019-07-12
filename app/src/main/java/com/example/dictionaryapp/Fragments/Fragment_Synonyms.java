package com.example.dictionaryapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dictionaryapp.R;
import com.example.dictionaryapp.WordMeaningActivity;

public class Fragment_Synonyms extends Fragment {
    public Fragment_Synonyms() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragments_definition,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textView);//Find textView Id

        String synonyms= ((WordMeaningActivity)context).synonyms;

        if(synonyms!=null)
        {
            synonyms = synonyms.replaceAll(",", ",\n");
            text.setText(synonyms);
        }
        if(synonyms==null)
        {
            text.setText(R.string.noSynFound);
        }


        return view;
    }
}
