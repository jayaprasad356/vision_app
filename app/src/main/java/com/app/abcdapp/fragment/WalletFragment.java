package com.app.abcdapp.fragment;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.TransactionAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.activities.MyRefundActivity;
import com.app.abcdapp.activities.SalaryAdvanceActivity;
import com.app.abcdapp.activities.WithdrawalActivity;
import com.app.abcdapp.databinding.SalaryAdvanceModelBinding;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Transanction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WalletFragment extends Fragment {

    Button btnWithdrawal,btnWithdrawalSalaryAdvance;
    RecyclerView recycler;
    TransactionAdapter transactionAdapter;
    Activity activity;
    Session session;
    TextView tvBalance,btnWallet,tvReferfund,tvCodeRefund,tvGrandTotal;
    LinearLayout calLi;
    private AdView mAdView;






    public WalletFragment() {
        // Required empty public constructor
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);
        btnWithdrawal = root.findViewById(R.id.btnWithdrawal);
        btnWithdrawalSalaryAdvance = root.findViewById(R.id.btnWithdrawalSalaryAdvance);
        recycler = root.findViewById(R.id.recycler);
        tvBalance = root.findViewById(R.id.tvBalance);
        btnWallet = root.findViewById(R.id.btnWallet);
        tvReferfund = root.findViewById(R.id.tvReferfund);
        tvCodeRefund = root.findViewById(R.id.tvCodeRefund);
        tvGrandTotal = root.findViewById(R.id.tvGrandTotal);

        calLi = root.findViewById(R.id.calLi);
        activity = getActivity();
        session = new Session(activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearLayoutManager);

        setWalletBalance();

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        walletApi();
        transactionList();

        if (session.getData(Constant.PROJECT_TYPE).equals("amail")){
            btnWallet.setText("Bonus Wallet = ₹500");

        }else {
            btnWallet.setText("Bonus Wallet = ₹2000");
        }

        btnWallet.setOnClickListener(view -> {
            apiBonuswallet("target_bonus");
            
        });



        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WithdrawalActivity.class);
                startActivity(intent);

            }
        });
        btnWithdrawalSalaryAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SalaryAdvanceActivity.class);
                startActivity(intent);

            }
        });

        if (Double.parseDouble(session.getData(Constant.TOTAL_REFUND)) >= 1800){
            calLi.setVisibility(View.INVISIBLE);
        }
        int r = session.getInt(Constant.TOTAL_CODES) / 3000;
        double codesRefund= (double) (session.getInt(Constant.TOTAL_CODES) * 0.03);
        double referRefund = Double.parseDouble(session.getData(Constant.TOTAL_REFERRALS)) * 250;
        tvCodeRefund.setText("Total Codes Refund Paid - Rs "+ String.format("%.2f", codesRefund));
        tvReferfund.setText("Total Refer Refund Paid   - Rs " + Integer.parseInt(session.getData(Constant.TOTAL_REFERRALS)) * 250);
        tvGrandTotal.setText("Grand Total Refund Paid  - Rs " + String.format("%.2f", codesRefund + referRefund));
        return root;

    }



    private void apiBonuswallet(String wallet_type)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.WALLET_TYPE, wallet_type);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }, activity, Constant.ADD_MAIN_BALANCE_URL, params, true);

    }

    private void addToMainBalance() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("Add to main Wallet", response + " - " + session.getData(Constant.SYNC_REFER_WALLET));
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONArray userArray = jsonObject.getJSONArray(Constant.DATA);
                        String refund_wallet = userArray.getJSONObject(0).getString(Constant.REFUND_WALLET);
                        session.setData(Constant.REFUND_WALLET, refund_wallet);
                        session.setData(Constant.BALANCE,userArray.getJSONObject(0).getString(Constant.BALANCE));
                        double current_bal = (double) (session.getInt(Constant.CODES) * 0.17);
                        tvBalance.setText("Main Balance = ₹"+session.getData(Constant.BALANCE) + " + "+String.format("%.2f", current_bal)+"");
                        String total_refund = userArray.getJSONObject(0).getString(Constant.TOTAL_REFUND);
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, activity, Constant.ADD_REFUND_TO_WALLET, params, false);
    }

    private void setWalletBalance() {
        double current_bal = (double) (session.getInt(Constant.CODES) * 0.17);
        if (session.getData(Constant.PROJECT_TYPE).equals("champion")){
            tvBalance.setText(session.getData(Constant.BALANCE) );
        }else {
            tvBalance.setText("Balance = ₹"+session.getData(Constant.BALANCE) + " + "+String.format("%.2f", current_bal)+"");

        }
        //tvminiwithdrawal.setText("Minimum Redeem =  ₹"+session.getData(Constant.MIN_WITHDRAWAL));

    }

    private void transactionList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        ArrayList<Transanction> transanctions = new ArrayList<>();
                        Gson g = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1 != null) {
                                Transanction group = g.fromJson(jsonObject1.toString(), Transanction.class);
                                transanctions.add(group);
                            } else {
                                break;
                            }
                        }
                        transactionAdapter = new TransactionAdapter(activity,transanctions);
                        recycler.setAdapter(transactionAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.TRNSACTION_LIST_URL, params, true);


    }


    public void walletApi()
    {
        if (ApiConfig.isConnected(activity)){
            Map<String, String> params = new HashMap<>();
            params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
            params.put(Constant.CODES,"0");
            ApiConfig.RequestToVolley((result, response) -> {
                Log.d("WALLET_RES",response);
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            session.setInt(Constant.TODAY_CODES,Integer.parseInt(jsonObject.getString(Constant.TODAY_CODES)));
                            session.setInt(Constant.TOTAL_CODES,Integer.parseInt(jsonObject.getString(Constant.TOTAL_CODES)));
                            session.setData(Constant.BALANCE, jsonObject.getString(Constant.BALANCE));
                            session.setData(Constant.STATUS, jsonObject.getString(Constant.STATUS));

                            setWalletBalance();

                        }else {

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.WALLET_URL, params, true);


        }




    }

}