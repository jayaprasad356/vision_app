package com.app.abcdapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.model.ImformationVideo;
import com.app.abcdapp.model.RewardUrls;

import java.util.ArrayList;


public class ImformationVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final Activity activity;
    ArrayList<ImformationVideo> imformationVideos;

    public ImformationVideoAdapter(Activity activity, ArrayList<ImformationVideo> imformationVideos) {
        this.activity = activity;
        this.imformationVideos = imformationVideos;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.rewards_url_layout, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final ImformationVideo imformationVideo = imformationVideos.get(position);

        holder.tvLanguage.setText(imformationVideo.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = imformationVideo.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);


            }
        });



    }


    @Override
    public int getItemCount() {
        return imformationVideos.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final TextView tvLanguage;
        final ImageButton ibPlaybtn;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            ibPlaybtn = itemView.findViewById(R.id.ibPlaybtn);


        }
    }
}