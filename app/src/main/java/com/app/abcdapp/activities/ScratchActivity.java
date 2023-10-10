package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScratchActivity extends AppCompatActivity {

    Dialog dialog;
    Button btnShare;
    TextView tvPrice,tvDate,tvVocherid;
    Activity activity;
    Session session;
    String vocher_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        activity = ScratchActivity.this;
        session = new Session(activity);

        btnShare = findViewById(R.id.btnShare);

         vocher_id = getIntent().getStringExtra("vocher_id");
        String date = getIntent().getStringExtra("date");
        String discount = getIntent().getStringExtra("discount");
        String status = getIntent().getStringExtra("status");
        String is_scratched = getIntent().getStringExtra("is_scratched");


        tvDate = findViewById(R.id.tvDate);
        tvVocherid = findViewById(R.id.tvVocherid);
        tvPrice = findViewById(R.id.tvPrice);


        tvDate.setText("Valid till - "+date);
        tvPrice.setText("Rs. "+discount);
        tvVocherid.setText("#"+vocher_id);

        View overlayView = findViewById(R.id.scratchView);

        if (is_scratched.equals("0")){
            overlayView.setVisibility(View.VISIBLE);


        }
        else {

            btnShare.setVisibility(View.VISIBLE);
            overlayView.setVisibility(View.GONE);

        }



        ScratchView scratchView = findViewById(R.id.scratchView);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                // Code to be executed when the ScratchView is fully revealed

                overlayView.setVisibility(View.GONE);
                btnShare.setVisibility(View.VISIBLE);
                scartch_finish();

                // Perform any other actions after revealing the ScratchView
                // For example:
                // btnShare.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                Log.d("Revealed", String.valueOf(percent));
            }
        });




        btnShare.setOnClickListener(v -> {

          share();
        });

    }

    private void share() {

        LinearLayout relativeLayout = findViewById(R.id.ll1);

        // Scale down the dimensions of the layout
        int scaleFactor = 2; // Scale down by a factor of 2
        int scaledWidth = relativeLayout.getMeasuredWidth() / scaleFactor;
        int scaledHeight = relativeLayout.getMeasuredHeight() / scaleFactor;

        try {
            // Create a bitmap for the scaled-down version of the layout
            Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(1f / scaleFactor, 1f / scaleFactor);
            relativeLayout.draw(canvas);

            // Save the Bitmap to a file
            File file = new File(getExternalCacheDir(), "image.png");
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Share the image using WhatsApp
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file));
            shareIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void scartch_finish() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.SCRATCH_ID, vocher_id);

        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("CAT_RES", response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("CAT_RES", response);

                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);



                    } else {
                        Toast.makeText(activity, "" + String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(requireActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        },activity, Constant.SCRATCH_CARD, params, true);
    }

}