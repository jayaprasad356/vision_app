package com.app.abcdapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.activities.ScratchActivity;
import com.app.abcdapp.fragment.FindMissingFragment;
import com.app.abcdapp.model.GenerateCodes;
import com.app.abcdapp.model.Scratch;

import java.util.ArrayList;


public class ScratchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final Activity activity;
    ArrayList<Scratch> scratches;
    Dialog dialog;
    FindMissingFragment findMissingFragment;

    public ScratchAdapter(Activity activity, ArrayList<Scratch> scratches) {
        this.activity = activity;
        this.scratches = scratches;

    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.scartch_layout, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final Scratch scratch = scratches.get(position);


        holder.tvPrice.setText("Rs. "+scratch.getDiscount());
        holder.tvVocherid.setText(scratch.getId());
        holder.tvDate.setText("Valid till -"+scratch.getExpiry_date());

        if (scratch.getIs_scratched().equals("0")){
            holder.ivImg.setVisibility(View.VISIBLE);

        }
        else {

            holder.ivImg.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent = new Intent(activity, ScratchActivity.class);
                intent.putExtra("vocher_id",scratch.getId());
                intent.putExtra("date",scratch.getExpiry_date());
                intent.putExtra("discount",scratch.getDiscount());
                intent.putExtra("status",scratch.getStatus());
                intent.putExtra("is_scratched",scratch.getIs_scratched());

                activity.startActivity(intent);

            }
        });



    }


    @Override
    public int getItemCount() {
        return scratches.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final TextView tvPrice, tvDate,tvVocherid;
        final ImageView ivImg;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvVocherid = itemView.findViewById(R.id.tvVocherid);
            ivImg = itemView.findViewById(R.id.ivImg);

        }
    }
}