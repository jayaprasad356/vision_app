package com.app.abcdapp.Adapter;

import static com.app.abcdapp.helper.Constant.PER_CODE_VAL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.R;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Levels;
import com.app.abcdapp.model.Transanction;

import java.util.ArrayList;
import java.util.logging.Level;

public class LevelsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Levels> levels;
    Session session;

    public LevelsAdapter(Activity activity, ArrayList<Levels> levels) {
        this.activity = activity;
        this.levels = levels;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.level_layout, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        session = new Session(activity);
        final ItemHolder holder = (ItemHolder) holderParent;
        final Levels level = levels.get(position);
        holder.tvLevel.setText(level.getLevel());
        double codeval = Double.parseDouble(level.getCost()) * 100;
        int extras = (Integer.parseInt(level.getLevel()) - 1) * 100;
        holder.tvCost.setText("Rs. "+codeval + "");
        int trial_level = Integer.parseInt(session.getData(Constant.LEVEL)) + 1;

        if (session.getData(Constant.STATUS).equals("1") && session.getBoolean(Constant.L_TRIAL_AVAILABLE) && level.getLevel().equals(trial_level + "") && !session.getBoolean(Constant.L_TRIAL_STATUS)){
            //holder.tvTrynow.setVisibility(View.VISIBLE);
            holder.tvTrynow.setOnClickListener(view -> {
                session.setBoolean(Constant.L_TRIAL_STATUS,true);
                session.setData(Constant.LEVEL, trial_level + "");
                if (session.getData(Constant.LEVEL).equals("5")){
                    session.setData(Constant.PER_CODE_VAL, "6");

                }
                else {
                    session.setData(Constant.PER_CODE_VAL, session.getData(Constant.LEVEL) + "");

                }
                Toast.makeText(activity, "Level Upgraded for next sync", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);

            });

        }

        if (!level.getLevel().equals("1")){
            holder.tvExtra.setVisibility(View.VISIBLE);
            if (level.getLevel().equals("5")){
                holder.tvExtra.setText("(Get 500 Codes Extra)");

            }else {
                holder.tvExtra.setText("(Get "+extras + " Codes Extra)");
            }


        }



        if (session.getData(Constant.LEVEL).equals(level.getLevel())){
            int color = ContextCompat.getColor(activity, R.color.newpink);
            holder.cvLevel.setCardBackgroundColor(color);


        }else {
            int color = ContextCompat.getColor(activity, in.aabhasjindal.otptextview.R.color.grey);
            holder.cvLevel.setCardBackgroundColor(color);


        }
        holder.cvLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!level.getLevel().equals("1")){
                    showAlertDialog(level.getCost(),level.getLevel(),level.getTotal_refers());
                }
            }
        });



    }

    private void showAlertDialog(String cost, String level, String total_refers) {

        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        // Get references to dialog views
        TextView tvLevel = dialogView.findViewById(R.id.tvLevel);
        TextView tvCodeRate = dialogView.findViewById(R.id.tvCodeRate);
        TextView tvTodayCodes = dialogView.findViewById(R.id.tvTodayCodes);
        TextView tvLevelEarnings = dialogView.findViewById(R.id.tvLevelEarnings);
        Button dialogButton = dialogView.findViewById(R.id.dialogButton);

        int today_codes_str = session.getInt(Constant.TODAY_CODES) +session.getInt(Constant.CODES);
        String codes_perValue_str = session.getData(PER_CODE_VAL);

        double today_codes = Double.parseDouble(today_codes_str+"");
        double codes_perValue = Double.parseDouble(codes_perValue_str);

        double level_earnings = today_codes * Double.parseDouble(cost);

        // Set dialog content
        tvLevel.setText("Level "+level);
        tvCodeRate.setText("Per Code Rate : "+cost );
        tvTodayCodes.setText("Today Codes : "+(session.getInt(Constant.TODAY_CODES) + session.getInt(Constant.CODES)));
        TextView tvmembers = dialogView.findViewById(R.id.tvmembers);
        tvLevelEarnings.setText("Today Codes Earnings : " + level_earnings);

        tvmembers.setText("Refer "+total_refers+" People - Earn Rs "+Integer.parseInt(total_refers) * 840+" Bonus & Also Get Below Benefits");
        AlertDialog dialog = builder.create();


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform any action when the dialog button is clicked

                // move to whatsapp
                sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp");

            }
        });


        // Show the dialog

        dialog.show();
    }




    @Override
    public int getItemCount() {
        return levels.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final TextView tvLevel,tvCost,tvExtra,tvTrynow;
        CardView cvLevel;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvCost = itemView.findViewById(R.id.tvCost);
            cvLevel = itemView.findViewById(R.id.cvLevel);
            tvExtra = itemView.findViewById(R.id.tvExtra);
            tvTrynow = itemView.findViewById(R.id.tvTrynow);



        }
    }



    private void sendMsgToWhatsapp(String mobile, String packageName) {
        String phoneNumber = "+91" + mobile;
        String message = "Hi"; // Replace with the message you want to send
        // Replace with the phone number you want to navigate to in WhatsApp

        PackageManager packageManager = activity.getPackageManager(); // Call packageManager on the activity instance
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message; // Include the message in the URL

            i.setPackage(packageName);
            i.setData(Uri.parse(url));

            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

}

