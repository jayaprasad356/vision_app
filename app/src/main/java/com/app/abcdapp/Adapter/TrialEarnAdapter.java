package com.app.abcdapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.model.Referandearn;
import com.app.abcdapp.model.Trialearn;

import java.util.ArrayList;


public class TrialEarnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final Activity activity;
    ArrayList<Trialearn> trialearns;



    public TrialEarnAdapter(Activity activity, ArrayList<Trialearn> trialearns) {
        this.activity = activity;
        this.trialearns = trialearns;

    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.trial_user_layout, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final Trialearn trialearn = trialearns.get(position);

        holder.tvName.setText(trialearn.getName());
        holder.tvMobile.setText(trialearn.getMobile());
        String regular = "";
        String champion = "";
        String statustexxt = "";
        if (trialearn.getChampion_trial().equals("0")){
            champion = "\nChampion Not Done";

        }else {
            champion = "\nChampion Done";


        }

        if (trialearn.getRegular_trial().equals("0")){
            regular = "Regular Not Done";

        }else {
            regular = "Regular Done";


        }
        holder.tvtaskCount.setText(regular + champion);
        holder.tvMobile.setText(trialearn.getMobile());
        if (trialearn.getValid().equals("1")){
            holder.tvStatus.setText("Valid");
            holder.tvStatus.setTextColor(Color.GREEN);

        }else {
            holder.tvStatus.setText("Invalid");
            holder.tvStatus.setTextColor(Color.RED);

        }




    }


    @Override
    public int getItemCount() {
        return trialearns.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final TextView tvName,tvMobile,tvStatus,tvtaskCount;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvtaskCount = itemView.findViewById(R.id.tvtaskCount);

        }
    }
}