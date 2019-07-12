package com.example.dictionaryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.HistoryViewHolder> {


    private ArrayList<History> histories;
    private Context context;

    public ArrayList<History> getHistories() {
        return histories;
    }

    public Context getContext() {
        return context;
    }

    public RecyclerViewAdapterHistory(ArrayList<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerViewAdapterHistory.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_layout, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterHistory.HistoryViewHolder historyViewHolder, int i) {
        historyViewHolder.enWord.setText(histories.get(i).getEn_word());
        historyViewHolder.enDef.setText(histories.get(i).getEn_def());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView enWord;
        TextView enDef;


        public HistoryViewHolder(View v) {
            super(v);
            enWord = (TextView) v.findViewById(R.id.en_word);
            enDef = (TextView) v.findViewById(R.id.en_def);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String text = histories.get(position).getEn_word();

                    Intent intent = new Intent(context, WordMeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word", text);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });


        }
    }
}
