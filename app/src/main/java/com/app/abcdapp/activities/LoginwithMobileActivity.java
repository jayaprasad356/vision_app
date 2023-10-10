package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginwithMobileActivity extends AppCompatActivity {


    EditText etMobile;
    Button loginbtn;
    Activity activity;
    Session session;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwith_mobile);

        activity = LoginwithMobileActivity.this;
        session = new Session(activity);


        etMobile = findViewById(R.id.etMobile);
        loginbtn = findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = etMobile.getText().toString();

                login(mobile);
            }
        });

    }

    private void login(String mobile) {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.MOBILE, mobile);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("user",response);


                        String mobile_registered =  jsonObject.getString(Constant.MOBILE_REGISTERED);

                       // Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, OtpActivity.class);
                        session.setData(Constant.MOBILE,mobile);
                        session.setData(Constant.MOBILE_REGISTERED,mobile_registered);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, activity, Constant.USER_LOGIN, params,false);




    }
}