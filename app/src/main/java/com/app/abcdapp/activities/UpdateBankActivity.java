package com.app.abcdapp.activities;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.TransactionAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Transanction;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateBankActivity extends AppCompatActivity {

    ImageView back;
    Button btnUpdateBank;
    Activity activity;
    EditText etAccountnum,etHolderName,etBankname,etBranch,etIFSC;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bank);
        activity = UpdateBankActivity.this;
        session = new Session(activity);
        back = findViewById(R.id.back);
        btnUpdateBank = findViewById(R.id.btnUpdateBank);
        etAccountnum = findViewById(R.id.etAccountnum);
        etHolderName = findViewById(R.id.etHolderName);
        etBankname = findViewById(R.id.etBankname);
        etBranch = findViewById(R.id.etBranch);
        etIFSC = findViewById(R.id.etIFSC);

        bankDetailsApi();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnUpdateBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAccountnum.getText().toString().trim().equals("")){
                    etAccountnum.setError("empty");
                    etAccountnum.requestFocus();
                }
                else if (etHolderName.getText().toString().trim().equals("")){
                    etHolderName.setError("empty");
                    etHolderName.requestFocus();
                }
                else if (etBankname.getText().toString().trim().equals("")){
                    etBankname.setError("empty");
                    etBankname.requestFocus();
                }
                else if (etBranch.getText().toString().trim().equals("")){
                    etBranch.setError("empty");
                    etBranch.requestFocus();
                }
                else if (!isValidIFSC(etIFSC.getText().toString().trim())){
                    etIFSC.setError("Please enter Valid IFSC Code");
                    etIFSC.requestFocus();
                }
                else {
                    updateBank();

                }

            }
        });
    }
    public boolean isValidIFSC(String ifsc) {
        // Regex pattern for IFSC code validation
        String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";

        // Check if the IFSC code matches the regex pattern
        if (ifsc.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    private void bankDetailsApi() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        etAccountnum.setText(jsonArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM));
                        etHolderName.setText(jsonArray.getJSONObject(0).getString(Constant.HOLDER_NAME));
                        etBankname.setText(jsonArray.getJSONObject(0).getString(Constant.BANK));
                        etBranch.setText(jsonArray.getJSONObject(0).getString(Constant.BRANCH));
                        etIFSC.setText(jsonArray.getJSONObject(0).getString(Constant.IFSC));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.BANK_DETAILS_URL, params, true);

    }

    private void updateBank() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        params.put(Constant.ACCOUNT_NUM,etAccountnum.getText().toString().trim());
        params.put(Constant.HOLDER_NAME,etHolderName.getText().toString().trim());
        params.put(Constant.BANK,etBankname.getText().toString().trim());
        params.put(Constant.BRANCH,etBranch.getText().toString().trim());
        params.put(Constant.IFSC,etIFSC.getText().toString().trim());
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("BAN_DETAILS_RES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray bankArray = jsonObject.getJSONArray(Constant.DATA);
                        session.setData(Constant.ACCOUNT_NUM,bankArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM));
                        session.setData(Constant.HOLDER_NAME,bankArray.getJSONObject(0).getString(Constant.HOLDER_NAME));
                        session.setData(Constant.BANK,bankArray.getJSONObject(0).getString(Constant.BANK));
                        session.setData(Constant.BRANCH,bankArray.getJSONObject(0).getString(Constant.BRANCH));
                        session.setData(Constant.IFSC,bankArray.getJSONObject(0).getString(Constant.IFSC));
                        startActivity(new Intent(activity, WithdrawalActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.UPDATE_BANK_URL, params, true);



    }
}