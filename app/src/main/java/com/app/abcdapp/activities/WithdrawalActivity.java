package com.app.abcdapp.activities;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.RedeemedAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Redeem;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WithdrawalActivity extends AppCompatActivity {



    ImageView back;
    RecyclerView recycler;
    RedeemedAdapter redeemedAdapter;
    Activity activity;
    Button btnUpdateBank,btnWithdrawal;
    TextView tvBalance;
    Session session;
    EditText etAmount;
    String withdrawal_type = "";
    TextView tvCodeBalance;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        activity = WithdrawalActivity.this;
        session = new Session(activity);

        back = findViewById(R.id.back);
        recycler = findViewById(R.id.recycler);
        btnUpdateBank = findViewById(R.id.btnUpdateBank);
        tvBalance = findViewById(R.id.tvBalance);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);
        etAmount = findViewById(R.id.etAmount);
//        tvminiwithdrawal = findViewById(R.id.tvminiwithdrawal);

        tvCodeBalance = findViewById(R.id.tvCodeBalance);




        CodeWallet();








        tvBalance.setText("Available Balance = ₹"+session.getData(Constant.BALANCE));
        tvCodeBalance.setText(session.getData(Constant.BALANCE));
        //tvminiwithdrawal.setText("Minimum Withdrawal =  ₹"+session.getData(Constant.MIN_WITHDRAWAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(linearLayoutManager);
        redeemlist();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnUpdateBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,UpdateBankActivity.class);
                startActivity(intent);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Balance;

                    Balance = session.getData(Constant.BALANCE);


                if (etAmount.getText().toString().trim().equals("") || etAmount.getText().toString().trim().equals("0")){
                    etAmount.setError("enter amount");
                    etAmount.requestFocus();
                }else if (Double.parseDouble(etAmount.getText().toString().trim()) > Double.parseDouble(Balance)) {
                    Toast.makeText(activity, "insufficient balance", Toast.LENGTH_SHORT).show();

                }else {
                    withdrawalApi();

                }


            }
        });
    }

    private void referWallet() {

        withdrawal_type = "refer_withdrawal";
    }

    private void CodeWallet() {

        withdrawal_type = "code_withdrawal";

    }


    private void withdrawalApi() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        params.put(Constant.AMOUNT,etAmount.getText().toString().trim());
        params.put(Constant.WITHDRAWAL_TYPE,withdrawal_type);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        session.setData(Constant.BALANCE,jsonObject.getString(Constant.BALANCE));
                        session.setData(Constant.REFER_BALANCE,jsonObject.getString(Constant.REFER_BALANCE));
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(false);
                        builder.setTitle("Withdrawal Successful\n" +
                                "You will get paid within 24 hrs");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(activity, MainActivity.class));
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }else {
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.WITHDRAWAL_URL, params, true);


    }

    private void redeemlist() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("WITHDRAWAL_RES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Redeem> redeems = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1 != null) {
                                Redeem group = g.fromJson(jsonObject1.toString(), Redeem.class);
                                redeems.add(group);
                            } else {
                                break;
                            }
                        }

                        redeemedAdapter = new RedeemedAdapter(activity,redeems);
                        recycler.setAdapter(redeemedAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.WITHDRAWAL_LIST_URL, params, true);





    }
}