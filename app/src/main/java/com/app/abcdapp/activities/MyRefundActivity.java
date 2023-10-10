package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.fragment.HomeFragment;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRefundActivity extends AppCompatActivity {
    Button btnAddMainWallet;
    Session session;
    Activity activity;
    TextView tvBalance;
    ImageView ivBack;
    String Amount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_refund);

        session = new Session(this);
        activity = MyRefundActivity.this;
        btnAddMainWallet = findViewById(R.id.btn);
        tvBalance = findViewById(R.id.tvBalance);
        ivBack=findViewById(R.id.backbtn);
        refundWallet();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnAddMainWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void refundWallet() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("REFUND_WALLET", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONArray userArray = jsonObject.getJSONArray(Constant.DATA);
                        String refund_wallet = userArray.getJSONObject(0).getString(Constant.REFUND_WALLET);
                        Amount = refund_wallet;
                        tvBalance.setText("Your Refund = "+refund_wallet);
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, activity, Constant.REFUND_WALLET_URL, params, false);


    }



}