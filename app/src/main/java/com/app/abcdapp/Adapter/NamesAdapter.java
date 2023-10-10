package com.app.abcdapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.fragment.FindMissingFragment;
import com.app.abcdapp.model.GenerateCodes;

import java.util.ArrayList;


public class NamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final Activity activity;
    ArrayList<GenerateCodes> generateCodes;
    Dialog dialog;
    FindMissingFragment findMissingFragment;

    public NamesAdapter(Activity activity, ArrayList<GenerateCodes> generateCodes, Dialog dialog, FindMissingFragment findMissingFragment) {
        this.activity = activity;
        this.generateCodes = generateCodes;
        this.dialog = dialog;
        this.findMissingFragment = findMissingFragment;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.names_list_layout, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final GenerateCodes cities1 = generateCodes.get(position);


        holder.tvName.setText(cities1.getStudent_name());
        holder.tvCodes.setText(cities1.getId_number());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                findMissingFragment.setNameValue(cities1.getStudent_name());

            }
        });



    }


    @Override
    public int getItemCount() {
        return generateCodes.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final TextView tvName, tvCodes;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCodes = itemView.findViewById(R.id.tvCodes);

        }
    }
}