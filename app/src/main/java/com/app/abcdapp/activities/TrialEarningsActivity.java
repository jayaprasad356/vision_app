package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.NotificationAdapter;
import com.app.abcdapp.Adapter.ReferandEarnAdapter;
import com.app.abcdapp.Adapter.TrialEarnAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Notification;
import com.app.abcdapp.model.Referandearn;
import com.app.abcdapp.model.Trialearn;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrialEarningsActivity extends AppCompatActivity {
    Button btnAddtoWallet;
    RecyclerView recyclerView;
    Activity activity;
    Session session;
    TrialEarnAdapter trialEarnAdapter;

    Button btnWithdrawal;
    ImageView imgBack;
    TextView tvEarnings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_earnings);
        activity = TrialEarningsActivity.this;
        session = new Session(activity);
        imgBack = findViewById(R.id.imgBack);
        btnAddtoWallet = findViewById(R.id.btnAddtoWallet);
        recyclerView = findViewById(R.id.recyclerView);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);
        tvEarnings = findViewById(R.id.tvEarnings);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
                ApiConfig.RequestToVolley((result, response) -> {
                    if (result) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean(Constant.SUCCESS)) {
                                Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, activity, Constant.ADD_TRIAL_TO_WALLET_URL, params, true);


            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });

        trialUserList();




    }
    private void trialUserList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    btnWithdrawal.setText("Withdraw ₹"+jsonObject.getString(Constant.TRIAL_WALLET));
                    tvEarnings.setText(""+jsonObject.getString(Constant.TOTAL_EARNINGS));
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Trialearn> trialearns = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Trialearn group = g.fromJson(jsonObject1.toString(), Trialearn.class);
                                trialearns.add(group);
                            } else {
                                break;
                            }
                        }

                        trialEarnAdapter = new TrialEarnAdapter(activity,trialearns);
                        recyclerView.setAdapter(trialEarnAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.MY_TRIALS_EARN_LIST_URL, params, true);

    }


}