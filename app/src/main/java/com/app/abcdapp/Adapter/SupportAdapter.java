package com.app.abcdapp.Adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Notification;
import com.app.abcdapp.model.Support;

import java.util.ArrayList;


public class SupportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Support> supports;
    Session session;

    public SupportAdapter(Activity activity, ArrayList<Support> supports) {
        this.activity = activity;
        this.supports = supports;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.support_item, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        session = new Session(activity);
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final Support support = supports.get(position);
        holder.title.setText(support.getTitle());
        holder.description.setText(support.getDescription());
        holder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp",support.getDescription());

                }catch (Exception e){

                }


            }
        });






    }
    private void sendMsgToWhatsapp(String mobile, String packageName,String message) {
        String phoneNumber = "+91" + mobile;

        PackageManager packageManager = activity.getPackageManager(); // Call packageManager on the activity instance
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message +"\n\n"; // Include the message in the URL

            i.setPackage(packageName);
            i.setData(Uri.parse(url));

            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return supports.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {
        final TextView title,description;
        final Button btnOpen;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            btnOpen = itemView.findViewById(R.id.btnOpen);



        }
    }
}

