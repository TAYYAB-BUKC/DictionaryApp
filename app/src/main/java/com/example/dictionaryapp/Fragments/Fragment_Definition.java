package com.example.dictionaryapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dictionaryapp.R;
import com.example.dictionaryapp.WordMeaningActivity;

public class Fragment_Definition extends Fragment {

    public Fragment_Definition() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragments_definition,container,false);

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textView);

        String en_definition= ((WordMeaningActivity)context).enDefinition;

        text.setText(en_definition);
        if(en_definition==null)
        {
            text.setText(R.string.nodefFound);
        }


        return view;
    }
}
