package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

public class ImageGenerationActivity extends AppCompatActivity {

    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_generation);

        session = new Session(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btn = findViewById(R.id.btnGenerate);
        TextView tvName = findViewById(R.id.tvName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvText2 = findViewById(R.id.tvtext2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvtext3 = findViewById(R.id.tvtext3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvtext4 = findViewById(R.id.tvtext4);
        try {
            tvName.setText(session.getData(Constant.NAME));
            tvText2.setText(session.getData(Constant.EARN));
            tvtext3.setText(session.getData(Constant.EARN));
            tvtext4.setText(session.getData(Constant.REFER_CODE));
//            tvName.setText(session.getData(Constant.URL));
//            tvText2.setText(session.getData(Constant.EARN));
//            tvtext3.setText(session.getData(Constant.WITHDRAWAL));
        }catch (Exception e) {
            Toast.makeText(this, "Could not generate certificate", Toast.LENGTH_SHORT).show();
        }
                AbsoluteLayout lyt = findViewById(R.id.Lyt);
                lyt.setDrawingCacheEnabled(true);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap map = Bitmap.createBitmap(lyt.getDrawingCache());
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),map,"title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("*/*");
                        i.putExtra(Intent.EXTRA_STREAM, bitmapUri );
                        startActivity(Intent.createChooser(i, "Share"));
                    }
                });
            }
}