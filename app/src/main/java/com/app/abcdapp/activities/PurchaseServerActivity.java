package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.abcdapp.R;
import com.app.abcdapp.fragment.HomeFragment;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

public class PurchaseServerActivity extends AppCompatActivity {
Button btnWhatsapp;
Session session;
ImageView ivBack;
 Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_server);
        btnWhatsapp=findViewById(R.id.btnWhatapp);
        ivBack=findViewById(R.id.backbtn);
        activity=this;

        session=new Session(this);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             openWhatsApp();
                }
            });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        }


    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone="+"91"+session.getData(Constant.WHATSAPP);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}